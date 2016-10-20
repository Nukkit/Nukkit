package cn.nukkit.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ServiceManager {
    private ConcurrentHashMap<Class<? extends Service>, ConcurrentLinkedQueue<Service>> services = new ConcurrentHashMap<>();

    private EconomyService defaultEconomy = new DefaultEconomyService(null);
    private PermissionService defaultPermission = new DefaultPermissionService(null);
    private ProtectionService defaultProtection = new DefaultProtectionService(null);

    public ServiceManager() {
    }

    public <T extends Service> void addService(Class<? extends Service> clazz, T service) {
        services.putIfAbsent(clazz, new ConcurrentLinkedQueue<>());
        ConcurrentLinkedQueue<Service> array = services.get(clazz);
        array.add(service);
    }

    public <T extends Service> Collection<T> getServices(Class<T> clazz) {
        return getServices(clazz, true);
    }

    public <T extends Service> Collection<T> getServices(Class<T> clazz, boolean includeDefaults) {
        ConcurrentLinkedQueue<Service> current = services.get(clazz);
        ArrayList<T> result = new ArrayList<>();
        if (current != null) {
            result.addAll((Collection<? extends T>) current);
        }
        if (includeDefaults) {
            if (clazz == EconomyService.class) {
                result.add((T) defaultEconomy);
            } else if (clazz == PermissionService.class) {
                result.add((T) defaultPermission);
            } else if (clazz == ProtectionService.class) {
                result.add((T) defaultProtection);
            }
        }
        return result;
    }
}
