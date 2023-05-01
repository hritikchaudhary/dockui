package ai.openfabric.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Entity
public class DockerNetwork {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "aliases")
    private String aliasesAsString;

    public List<String> getAliases() {
        if (aliasesAsString == null) {
            return Collections.emptyList();
        }
        return Arrays.asList(aliasesAsString.split(","));
    }

    public void setAliases(List<String> aliases) {
        if (aliases == null) {
            this.aliasesAsString = null;
        } else {
            this.aliasesAsString = String.join(",", aliases);
        }
    }


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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DockerNetwork)) return false;
        DockerNetwork that = (DockerNetwork) o;
        return Objects.equals(endpointId, that.endpointId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(endpointId);
    }

}
