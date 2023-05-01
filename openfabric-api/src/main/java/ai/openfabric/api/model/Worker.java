package ai.openfabric.api.model;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    @Column(name = "created_at")
    private Timestamp createdAt;

    @Getter
    @Setter
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Getter
    @Setter
    @Column(name = "deleted_at")
    private Timestamp deletedAt;

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
    @Column(name = "container_id")
    private String containerId;


    @Getter
    @OneToMany(mappedBy = "worker", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<DockerPort> ports = new ArrayList<>();

    public void setPorts(List<DockerPort> ports) {
        if (this.ports == null) {
            this.ports = new ArrayList<>();
        }
        for (DockerPort port : ports) {
            boolean portExists = false;
            for (DockerPort existingPort : this.ports) {
                if (existingPort.getPrivatePort() == port.getPrivatePort()) {
                    portExists = true;
                    break;
                }
            }
            if (!portExists) {
                port.setWorker(this);
                this.ports.add(port);
            }
        }
    }


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
    @OneToMany(mappedBy = "worker", cascade = CascadeType.ALL)
    private List<DockerMount> mounts = new ArrayList<>();

    public void setMounts(List<DockerMount> mounts) {
        if (mounts == null) {
            this.mounts = new ArrayList<>();
            return;
        }

        Set<DockerMount> mountSet = new HashSet<>(mounts);
        this.mounts = new ArrayList<>(mountSet);

        for (DockerMount mount : this.mounts) {
            mount.setWorker(this);
        }
    }

}
