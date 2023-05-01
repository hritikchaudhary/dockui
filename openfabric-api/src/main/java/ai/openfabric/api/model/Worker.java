package ai.openfabric.api.model;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Entity()
@Table(name = "worker")
public class Worker extends Datable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "of-uuid")
    @GenericGenerator(name = "of-uuid", strategy = "ai.openfabric.api.model.IDGenerator")
    @Getter
    @Setter
    public String id;

    @Getter
    @Setter
    @Column(name = "name")
    public String name;

    @Getter
    @Setter
    @Column(name = "command")
    private String command;

    @Getter
    @Setter
    @Column(name = "created")
    private long created;

    @Getter
    @Setter
    @Column(name = "image")
    private String image;

    @Getter
    @Setter
    @Column(name = "image_id")
    private String imageId;


    @Getter
    @Setter
    @OneToMany(mappedBy = "dockerContainer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DockerPort> ports;

    @Getter
    @Setter
    @ElementCollection
    @Column(name = "labels")
    private Map<String, String> labels;

    @Getter
    @Setter
    @Column(name = "status")
    private String status;

    @Getter
    @Setter
    @Column(name = "state")
    private String state;

    @Getter
    @Setter
    @Column(name = "size_rw")
    private Long sizeRw;

    @Getter
    @Setter
    @Column(name = "size_root_fs")
    private Long sizeRootFs;

    @Getter
    @Setter
    @OneToOne(mappedBy = "worker", cascade = CascadeType.ALL)
    private DockerHostConfig hostConfig;

    @Getter
    @Setter
    @OneToOne(mappedBy = "worker", cascade = CascadeType.ALL)
    private DockerNetworkSettings networkSettings;

    @Getter
    @Setter
    @OneToMany(mappedBy = "worker", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DockerMount> mounts;

}
