import com.damo.proto.*
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import java.util.concurrent.TimeUnit
import kotlin.system.measureTimeMillis

class OrderClient(val channel: ManagedChannel) {
    private val stub = OrderServiceGrpc.newBlockingStub(channel)

    fun getOrders(totalOrdersToGenerate: Int = 5, totalItemsPerOrder: Int = 10): List<Order> {
        val request = orderRequest {
            totalOrders = totalOrdersToGenerate
            totalItems = totalItemsPerOrder
        }
        val response = stub.getOrders(request)
        return response.ordersList
    }

    fun close() {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
    }
}


fun main() {
    val channel = ManagedChannelBuilder.forTarget("localhost:50051")
        .usePlaintext()
        .build()
    val client = OrderClient(channel)
    val timeMilliseconds = measureTimeMillis {
        println(
            "Orders result: ${
                client.getOrders(
                    totalOrdersToGenerate = 2,
                    totalItemsPerOrder = 5
                )
            }"
        )
    }
    println("Time taken: $timeMilliseconds ms")
    client.close()
}

