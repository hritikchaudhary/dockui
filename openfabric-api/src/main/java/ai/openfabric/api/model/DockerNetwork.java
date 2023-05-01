package ai.openfabric.api.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
public class DockerNetwork {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    private String ipamConfig;

    @Getter
    @Setter
    @ElementCollection
    private List<String> links;

    @Getter
    @Setter
    @ElementCollection
    private List<String> aliases;


    @Getter
    @Setter
    private String endpointId;

    @Getter
    @Setter
    private String gateway;

    @Getter
    @Setter
    private String ipAddress;

    @Getter
    @Setter
    private int ipPrefixLen;

    @Getter
    @Setter
    private String ipv6Gateway;

    @Getter
    @Setter
    private String globalIpv6Address;

    @Getter
    @Setter
    private int globalIpv6PrefixLen;

    @Getter
    @Setter
    private String macAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "network_id")
    @Getter
    @Setter
    private DockerNetworkSettings dockerNetworkSettings;
}
