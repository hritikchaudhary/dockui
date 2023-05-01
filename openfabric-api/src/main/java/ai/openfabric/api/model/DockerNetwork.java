package ai.openfabric.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @Column(name = "ipam_config")
    private String ipamConfig;

    @Getter
    @Setter
    @ElementCollection
    @Column(name = "links")
    private List<String> links;

    @Getter
    @Setter
    @ElementCollection
    @Column(name = "aliases")
    private List<String> aliases;


    @Getter
    @Setter
    @Column(name = "endpoint_id")
    private String endpointId;

    @Getter
    @Setter
    @Column(name = "gateway")
    private String gateway;

    @Getter
    @Setter
    @Column(name = "ip_address")
    private String ipAddress;

    @Getter
    @Setter
    @Column(name = "ip_prefix_len")
    private int ipPrefixLen;

    @Getter
    @Setter
    @Column(name = "ipv6_gateway")
    private String ipv6Gateway;

    @Getter
    @Setter
    @Column(name = "global_ipv6_address")
    private String globalIpv6Address;

    @Getter
    @Setter
    @Column(name = "global_ipv6_prefix_len")
    private int globalIpv6PrefixLen;

    @Getter
    @Setter
    @Column(name = "mac_address")
    private String macAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "network_id")
    @Getter
    @Setter
    @JsonIgnore
    private DockerNetworkSettings dockerNetworkSettings;
}
