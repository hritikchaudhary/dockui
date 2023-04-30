package ai.openfabric.api.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
public class DockerPort {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    private String ip;

    @Getter
    @Setter
    private int privatePort;

    @Getter
    @Setter
    private int publicPort;

    @Getter
    @Setter
    private String type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_id")
    private Worker worker;
}
