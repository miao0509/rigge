package white.common;

public class BaseUtils {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();
    public static void setCurrentThreadId(Long id){
        threadLocal.set(id);
    }

    public static Long getCurrentThreadId(){
        return threadLocal.get();
    }
}
