package ai.openfabric.api.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
public class DockerHostConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    private String networkMode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_id")
    private Worker worker;
}
