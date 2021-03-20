package com.google.gdata.util.common.base;

public interface Escaper {
    Appendable escape(Appendable appendable);

    String escape(String str);
}
