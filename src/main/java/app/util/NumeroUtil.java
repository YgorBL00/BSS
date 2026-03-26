package app.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class NumeroUtil {

    private static final DecimalFormat df;

    static {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("pt", "BR"));
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');

        df = new DecimalFormat("0.00", symbols);
    }

    public static String formatar(double valor) {
        return df.format(valor);
    }

    // NOVO MÉTODO
    public static double converter(String texto) {

        if (texto == null || texto.isEmpty()) return 0;

        texto = texto.replace("R$", "")
                .replace(".", "")
                .replace(",", ".")
                .trim();

        return Double.parseDouble(texto);
    }
}