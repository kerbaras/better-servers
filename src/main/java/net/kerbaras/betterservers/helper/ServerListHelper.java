package net.kerbaras.betterservers.helper;

import net.kerbaras.betterservers.data.BetterServerData;
import net.minecraft.client.multiplayer.ServerData;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ServerListHelper {

    public static List<BetterServerData> load(List<ServerData> vanillaServerList, List<BetterServerData> betterServerList){

        // use ip to distinguish servers
        Map<String, List<BetterServerData>> betterServerListByIp = betterServerList
                .stream()
                .collect(Collectors.groupingBy(info -> info.ip));
        Map<String, List<BetterServerData>> betterServerListByName = betterServerList
                .stream()
                .collect(Collectors.groupingBy(info -> info.name));

        // For each server in vanilla:
        return vanillaServerList.stream().map(server -> {
            Optional<BetterServerData> betterServer =
                    // if possible match by ip
                    findHit(betterServerListByIp.get(server.ip), candidate -> candidate.name.equals(server.name))
                            // otherwise match by name
                    .or(() -> findHit(betterServerListByName.get(server.name), candidate -> candidate.ip.equals(server.ip)));

            return augmentData(server, betterServer.get());
        }).collect(Collectors.toList());
    }

    private static Optional<BetterServerData> findHit(List<BetterServerData> candidates, Predicate<BetterServerData> predicate){
        // if there's only 1 option, then use that
        if (candidates.size() == 1) return Optional.of(candidates.get(0));

        // If there's more, match by closest
        return candidates
                .stream()
                .filter(predicate)
                .findFirst();
    }

    private static BetterServerData augmentData(ServerData vanilla, @Nullable BetterServerData saved) {
        // always prefer vanilla attributes
        BetterServerData data = BetterServerData.fromVanilla(vanilla);
        if (saved == null) return data;

        // aggregate extra info
        data.id = saved.id;
        for (String label : saved.labels) {
            data.labels.add(label);
        }
        return data;
    }
}
