package com.diandi.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by sulinger  .
 * date:14-9-13 .
 * time:13:58 .
 * project:DianDi1.1.0 .
 * Copyright © sulinger .All Rights Reserved.
 */
@SuppressWarnings("unckecked")
public class SortList<E> {
    public void Sort(List<E> list, final String method, final String sort) {
        Collections.sort(list, new ListComparator(method, sort));
    }

    private class ListComparator implements Comparator<E> {

        private String method;
        private String sort;

        public ListComparator(String method, String sort) {
            this.method = method;
            this.sort = sort;
        }


        public int compare(E a, E b) {
            int ret = 0;

            try {
                Method m1 = ((E)a).getClass().getMethod(method);
                Method m2 = ((E) b).getClass().getMethod(method);
                if (sort != null && "desc".equals(sort))//倒序
                    ret = m2.invoke(((E) b)).toString().compareTo(m1.invoke(((E) a)).toString());
                else//正序
                    ret = m1.invoke(((E) a)).toString().compareTo(m2.invoke(((E) b)).toString());
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

            return ret;
        }
    }
}
