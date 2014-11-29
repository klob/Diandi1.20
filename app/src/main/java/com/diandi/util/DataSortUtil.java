package com.diandi.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * *******************************************************************************
 * *********    Author : klob(kloblic@gmail.com) .
 * *********    Date : 2014-11-29  .
 * *********    Time : 11:46 .
 * *********    Project name : Diandi1.18 .
 * *********    Version : 1.0
 * *********    Copyright @ 2014, klob, All Rights Reserved
 * *******************************************************************************
 */
@SuppressWarnings("unckecked")
public class DataSortUtil<E> {
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
                Method m1 = ((E) a).getClass().getMethod(method);
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
