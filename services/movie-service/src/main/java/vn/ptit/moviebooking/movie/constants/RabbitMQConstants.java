package vn.ptit.moviebooking.movie.constants;

public interface RabbitMQConstants {

    interface Queue {
        String MOVIE_VALIDATE_COMMAND = "queue.movies.command";
    }

    interface RoutingKey {
        String MOVIE_VALIDATE_REPLY = "routingKey.movies.reply";
    }

    interface Exchange {
        String DIRECT_EXCHANGE = "amq.direct";
    }
}
