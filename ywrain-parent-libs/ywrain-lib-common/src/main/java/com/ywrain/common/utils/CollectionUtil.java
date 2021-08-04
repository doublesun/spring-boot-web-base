package com.ywrain.common.utils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 集合工具类
 * <pre>
 *     复制Apache Commons组件实现
 * </pre>
 *
 * @author weipengfei@youcheyihou.com
 */
public abstract class CollectionUtil {

    private static void checkIndexBounds(int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("Index cannot be negative: " + index);
        }
    }


    public static boolean isEmpty(Collection<?> coll) {
        return coll == null || coll.isEmpty();
    }

    public static boolean isNotEmpty(Collection<?> coll) {
        return !isEmpty(coll);
    }

    public static <T> T get(Iterable<T> iterable, int index) {
        checkIndexBounds(index);
        return iterable instanceof List ? (T)((List)iterable).get(index) : get(iterable.iterator(), index);
    }

    public static <T> T get(Iterator<T> iterator, int index) {
        int i = index;
        checkIndexBounds(index);

        while(iterator.hasNext()) {
            --i;
            if (i == -1) {
                return iterator.next();
            }

            iterator.next();
        }

        throw new IndexOutOfBoundsException("Entry does not exist: " + i);
    }

    public static <C> boolean addAll(Collection<C> collection, Iterator<? extends C> iterator) {
        boolean changed;
        for(changed = false; iterator.hasNext(); changed |= collection.add(iterator.next())) {
        }

        return changed;
    }

    public static <C> boolean addAll(Collection<C> collection, C[] elements) {
        boolean changed = false;
        Object[] arr$ = elements;
        int len$ = elements.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            C element = (C)arr$[i$];
            changed |= collection.add(element);
        }

        return changed;
    }

    public static <T> boolean addIgnoreNull(Collection<T> collection, T object) {
        if (collection == null) {
            throw new NullPointerException("The collection must not be null");
        } else {
            return object != null && collection.add(object);
        }
    }

    public static int size(Object object) {
        if (object == null) {
            return 0;
        } else {
            int total = 0;
            if (object instanceof Map) {
                total = ((Map)object).size();
            } else if (object instanceof Collection) {
                total = ((Collection)object).size();
            } else if (object instanceof Object[]) {
                total = ((Object[]) object).length;
            } else if (object instanceof Iterator) {
                Iterator it = (Iterator)object;

                while(it.hasNext()) {
                    ++total;
                    it.next();
                }
            } else if (object instanceof Enumeration) {
                Enumeration it = (Enumeration)object;

                while(it.hasMoreElements()) {
                    ++total;
                    it.nextElement();
                }
            } else {
                try {
                    total = Array.getLength(object);
                } catch (IllegalArgumentException var3) {
                    throw new IllegalArgumentException("Unsupported object type: " + object.getClass().getName());
                }
            }

            return total;
        }
    }

    public static <E> List<E> retainAll(Collection<E> collection, Collection<?> retain) {
        List<E> list = new ArrayList(Math.min(collection.size(), retain.size()));
        Iterator i$ = collection.iterator();

        while(i$.hasNext()) {
            E obj = (E)i$.next();
            if (retain.contains(obj)) {
                list.add(obj);
            }
        }

        return list;
    }

    public static <E> List<E> removeAll(Collection<E> collection, Collection<?> remove) {
        List<E> list = new ArrayList();
        Iterator i$ = collection.iterator();

        while(i$.hasNext()) {
            E obj = (E)i$.next();
            if (!remove.contains(obj)) {
                list.add(obj);
            }
        }

        return list;
    }
}
