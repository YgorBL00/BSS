//package app.update;
//
//import org.update4j.Configuration;
//import org.update4j.UpdateResult;
//
//import java.io.InputStreamReader;
//import java.net.URL;
//
//public class Atualizador {
//
//    public static void verificar(String configUrl) {
//
//        try {
//
//            URL url = new URL(configUrl);
//
//            Configuration config =
//                    Configuration.read(new InputStreamReader(url.openStream()));
//
//            UpdateResult result = config.update();
//
//            if (result.getException() == null) {
//
//                System.out.println("Sistema atualizado!");
//
//            } else {
//
//                result.getException().printStackTrace();
//
//            }
//
//        } catch (Exception e) {
//
//            e.printStackTrace();
//
//        }
//    }
//}