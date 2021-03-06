package com.redhat.ceylon.common;

public enum Backend {
    Java("jvm", BackendSupport.JAVA),
    JavaScript("js", BackendSupport.JAVASCRIPT);
    
    public final String nativeAnnotation;
    public final BackendSupport backendSupport;
    
    Backend(String nativeAnnotation, BackendSupport backendSupport) {
        this.nativeAnnotation = nativeAnnotation;
        this.backendSupport = backendSupport;
    }
    
    public static boolean validAnnotation(String backend) {
        return fromAnnotation(backend) != null;
    }
    
    public static Backend fromAnnotation(String backend) {
        for (Backend b : Backend.values()) {
            if (b.nativeAnnotation.equals(backend)) {
                return b;
            }
        }
        return null;
    }
    
    public static String annotations() {
        String result = "";
        for (Backend b : Backend.values()) {
            if (!result.isEmpty()) {
                result += ", ";
            }
            result += b.nativeAnnotation;
        }
        return result;
    }
}