import java.net.SocketException;
import java.nio.file.AccessDeniedException;
import java.rmi.server.ExportException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public class  HomeWork3 {

    public static void raiseException(Integer exceptionId) throws SocketException, ClassNotFoundException, AccessDeniedException, ExportException {
        if (exceptionId == 1) {
            throw new SocketException();
        }
        else if (exceptionId == 2) {
            throw new ClassNotFoundException();
        }
        else if (exceptionId == 3) {
            throw new AccessDeniedException("Ошибка");
        }
        else if (exceptionId == 4) {
            throw new ExportException("Ошибка");
        }
    }

    public static String catchException(Integer integer) {
        try {
            raiseException(integer);
        } catch (SocketException ex) {
            return "SocketException";
        } catch (ClassNotFoundException ex) {
            return "ClassNotFoundException";
        } catch (AccessDeniedException ex) {
            return "AccessDeniedException";
        } catch (ExportException ex) {
            return "ExportException";
        }
        return "Exception";
    }

    public static void main(String[] args) {
        testFirstTask();
        testSecondTask();
    }


    private static final Map<Integer, Class<? extends Exception>> classesCorrelation = Map.of(
            1, SocketException.class,
            2, ClassNotFoundException.class,
            3, AccessDeniedException.class,
            4, ExportException.class
    );

    public static void testFirstTask() {
        System.out.println("\nTests for raiseException");
        AntiCheat.run();
        for (Integer exceptionId : classesCorrelation.keySet().stream().sorted().toList()) {
            try {
                raiseException(exceptionId);
                printTestCase(exceptionId, "Должна была выкинуться ошибка", "Ошибка не выкинулась", 45);
            } catch(Exception e) {
                printTestCase(exceptionId, classesCorrelation.get(exceptionId).toString(), e.getClass().toString(), 45);
            }
        }
    }

    public static void testSecondTask() {
        System.out.println("\nTests for catchException"); AntiCheat.run();
        Function<String, String> f = str -> {List<String> r = Arrays.stream(str.split("\\.")).toList();return r.get(r.size() - 1);};
        for (Integer exceptionId : classesCorrelation.keySet().stream().sorted().toList()) printTestCase(exceptionId, f.apply(classesCorrelation.get(exceptionId).toString()), catchException(exceptionId), 30);
    }

    public static class AntiCheat {
        public static void run() {
            StringBuilder sb = new StringBuilder("");
            List<String> antiCheatList = new ArrayList<>();
            antiCheatList.addAll(classesCorrelation.keySet().stream().map(Object::toString).toList());
            antiCheatList.addAll(classesCorrelation.values().stream().map(Object::toString).toList());
            antiCheatList.add(sb.toString());
            calcHash(antiCheatList);
        };

        public static String bytesToHex(byte[] bytes) {
            char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
            char[] hexChars = new char[bytes.length * 2];
            for (int j = 0; j < bytes.length; j++) {
                int v = bytes[j] & 0xFF;
                hexChars[j * 2] = HEX_ARRAY[v >>> 4];
                hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
            }
            return new String(hexChars);
        }

        public static void calcHash(List<String> list) {
            String total = String.join("", list);
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(total.getBytes());
                byte[] digest = md.digest();
                System.out.println("AntiCheatCheck: " + bytesToHex(digest));
            } catch (NoSuchAlgorithmException ignored) {}
        }
    }

    public static String constLen(String str, int len) {
        StringBuilder sb = new StringBuilder(str);
        while (len-- - str.length() > 0) sb.append(" ");
        return sb.toString();
    }

    public static void printTestCase(int n, String exp, String act, int minLen) {
        Function<String, String> green = str -> "\u001B[34m" + str + "\u001B[0m";
        Function<String, String> yellow = str -> "\u001B[33m" + str + "\u001B[0m";
        System.out.print( "TEST CASE " + constLen(String.valueOf(n), 4));
        System.out.print( "Ожидание: " + yellow.apply(constLen(exp, minLen)) + " Реальность: " + green.apply(constLen(act, minLen) + " "));
        if (Objects.equals(exp, act)) System.out.print("✅"); else System.out.print("❌");
        System.out.println();
    }

}