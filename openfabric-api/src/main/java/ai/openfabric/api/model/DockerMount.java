package ai.openfabric.api.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
public class DockerMount {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Getter
        @Setter
        private String type;

        @Getter
        @Setter
        private String name;

        @Getter
        @Setter
        private String source;

        @Getter
        @Setter
        private String destination;

        @Getter
        @Setter
        private String driver;

        @Getter
        @Setter
        private String mode;

        @Getter
        @Setter
        private boolean rw;

        @Getter
        @Setter
        private String propagation;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "worker_id")
        @Getter
        @Setter
        private Worker worker;
}
