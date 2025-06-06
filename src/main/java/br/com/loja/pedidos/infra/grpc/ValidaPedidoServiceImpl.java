package br.com.loja.pedidos.infra.grpc;

import br.com.loja.grpc.ValidaPedidoRequest;
import br.com.loja.grpc.ValidaPedidoResponse;
import br.com.loja.pedidos.domain.DomainServices;
import br.com.loja.pedidos.domain.entities.PedidoEntity;
import io.grpc.stub.StreamObserver;
import br.com.loja.grpc.ValidaPedidoServiceGrpc;
import net.devh.boot.grpc.server.service.GrpcService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@GrpcService
@RequiredArgsConstructor
public class ValidaPedidoServiceImpl extends ValidaPedidoServiceGrpc.ValidaPedidoServiceImplBase {

    private final DomainServices domainServices;

    /**
     * Método gRPC que verifica se o pedido com o ID fornecido existe.
     * Retorna true se existir, false caso contrário.
     */
    @Override
    public void retornaTrueSeExiste(ValidaPedidoRequest request, StreamObserver<ValidaPedidoResponse> responseObserver) {
        long pedidoId = request.getId();
        log.info("Recebida requisição para validar existência do pedido com ID: {}", pedidoId);

        boolean existe = false;
        try {
            PedidoEntity pedido = domainServices.buscaPedidoPorId(pedidoId);
            existe = (pedido != null);
        } catch (Exception e) {
            log.error("Erro ao buscar pedido com ID {}: {}", pedidoId, e.getMessage(), e);
        }

        ValidaPedidoResponse response = ValidaPedidoResponse.newBuilder()
                .setSuccess(existe)
                .build();

        log.info("Resposta para pedido ID {}: {}", pedidoId, existe);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

}