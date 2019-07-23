package vn.edu.topica.services;

import java.util.List;
import java.util.Map;

public interface UserService<K, V> {
    int getSumPoint(List<V> vList);

    Map<K, List<V>> getUsersByRole(List<V> vList);

    long count(List<V> vList);

    Map<Integer, V> convertToMapByPoint(List<V> vList, int point);
}

