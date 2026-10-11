import java.math.BigInteger;
import java.util.Scanner;

/**
 * ============================================================================
 * UNIVERSIDAD / ASIGNATURA: Arquitectura de Computadoras
 * TAREA: Deber Semana 2 - Conversor entre sistemas de numeración
 * ============================================================================
 * 
 * DESCRIPCIÓN:
 * Programa que convierte un número expresado en cualquier base (2 a 36) a
 * otra base de destino (2 a 36) implementando la lógica manual de los algoritmos:
 * - Conversión a Decimal: Método de Horner (evaluación polinómica).
 * - Conversión a Base Destino: Divisiones sucesivas y residuos invertidos.
 * 
 * MODULARIDAD Y RESPONSABILIDAD ÚNICA:
 * - validarBase()              : Valida que la base esté entre 2 y 36.
 * - validarNumero()            : Verifica que cada símbolo sea válido para la base.
 * - baseADecimal()             : Algoritmo manual Base Origen -> Decimal.
 * - decimalABase()             : Algoritmo manual Decimal -> Base Destino.
 * - convertir()                : Orquesta el proceso completo de conversión.
 * - ejecutarPruebas()          : Suite de pruebas automáticas con los casos del deber.
 * ============================================================================
 */
public class ConversorBasesApp {

    public static final int BASE_MINIMA = 2;
    public static final int BASE_MAXIMA = 36;

    // ========================================================================
    // 1. MÉTODOS DE VALIDACIÓN
    // ========================================================================

    /**
     * Valida que la base se encuentre en el rango permitido [2, 36].
     */
    public static void validarBase(int base) {
        if (base < BASE_MINIMA || base > BASE_MAXIMA) {
            throw new IllegalArgumentException(String.format(
                "La base ingresada (%d) es inválida. Debe ser un número entero entre %d y %d.",
                base, BASE_MINIMA, BASE_MAXIMA
            ));
        }
    }

    /**
     * Valida que el número no esté vacío y que cada carácter pertenezca a la base de origen.
     */
    public static void validarNumero(String numero, int base) {
        if (numero == null || numero.trim().isEmpty()) {
            throw new IllegalArgumentException("El número a convertir no puede estar vacío.");
        }

        String limpio = numero.trim();
        int inicio = 0;

        // Soporte de signo opcional (+ o -)
        if (limpio.startsWith("+") || limpio.startsWith("-")) {
            if (limpio.length() == 1) {
                throw new IllegalArgumentException("El número solo contiene un signo y ningún dígito.");
            }
            inicio = 1;
        }

        // Validar símbolo por símbolo
        for (int i = inicio; i < limpio.length(); i++) {
            char c = limpio.charAt(i);
            int valor = caracterAValor(c);

            if (valor == -1) {
                throw new IllegalArgumentException(String.format(
                    "El símbolo '%c' no es válido. Solo se permiten dígitos (0-9) y letras (A-Z).", c
                ));
            }

            if (valor >= base) {
                char simboloMaximo = valorACaracter(base - 1);
                throw new IllegalArgumentException(String.format(
                    "El dígito '%c' (valor: %d) no existe en la base %d. En base %d solo se permiten valores entre '0' y '%c'.",
                    c, valor, base, base, simboloMaximo
                ));
            }
        }
    }

    // ========================================================================
    // 2. CONVERSIÓN DE SÍMBOLOS A VALORES Y VICEVERSA
    // ========================================================================

    /**
     * Convierte un carácter alfanumérico a su valor numérico (0-35).
     * Acepta mayúsculas y minúsculas: '0'-'9' -> 0-9, 'A'-'Z' / 'a'-'z' -> 10-35.
     */
    public static int caracterAValor(char c) {
        if (c >= '0' && c <= '9') return c - '0';
        if (c >= 'A' && c <= 'Z') return c - 'A' + 10;
        if (c >= 'a' && c <= 'z') return c - 'a' + 10;
        return -1;
    }

    /**
     * Convierte un valor numérico (0-35) a su carácter alfanumérico en mayúscula.
     */
    public static char valorACaracter(int valor) {
        if (valor >= 0 && valor <= 9) return (char) ('0' + valor);
        if (valor >= 10 && valor <= 35) return (char) ('A' + (valor - 10));
        throw new IllegalArgumentException("Valor fuera de rango (0-35): " + valor);
    }

    // ========================================================================
    // 3. ALGORITMOS DE CONVERSIÓN (MANUALES, SIN FUNCIONES PREDEFINIDAS)
    // ========================================================================

    /**
     * Convierte manualmente un número de base origen a decimal usando el Método de Horner.
     * Algoritmo: acum = (acum * base) + digito
     */
    public static BigInteger baseADecimal(String numero, int baseOrigen) {
        String limpio = numero.trim();
        boolean esNegativo = limpio.startsWith("-");
        int inicio = (esNegativo || limpio.startsWith("+")) ? 1 : 0;

        BigInteger decimal = BigInteger.ZERO;
        BigInteger base = BigInteger.valueOf(baseOrigen);

        for (int i = inicio; i < limpio.length(); i++) {
            int digito = caracterAValor(limpio.charAt(i));
            decimal = decimal.multiply(base).add(BigInteger.valueOf(digito));
        }

        return esNegativo ? decimal.negate() : decimal;
    }

    /**
     * Convierte manualmente un número decimal a la base destino mediante divisiones sucesivas.
     * Algoritmo:
     * - residuo = decimal % baseDestino (dígito de menor a mayor peso)
     * - decimal = decimal / baseDestino
     * - Al finalizar, se invierte la cadena de residuos.
     */
    public static String decimalABase(BigInteger decimal, int baseDestino) {
        if (decimal.equals(BigInteger.ZERO)) {
            return "0";
        }

        boolean esNegativo = decimal.signum() < 0;
        BigInteger actual = decimal.abs();
        BigInteger base = BigInteger.valueOf(baseDestino);
        StringBuilder resultadoInvertido = new StringBuilder();

        while (actual.compareTo(BigInteger.ZERO) > 0) {
            BigInteger[] division = actual.divideAndRemainder(base);
            int residuo = division[1].intValue();
            resultadoInvertido.append(valorACaracter(residuo));
            actual = division[0];
        }

        String res = resultadoInvertido.reverse().toString();
        return esNegativo ? "-" + res : res;
    }

    /**
     * Orquesta el proceso completo: validaciones y conversión de base origen a destino.
     */
    public static String convertir(String numero, int baseOrigen, int baseDestino) {
        validarBase(baseOrigen);
        validarBase(baseDestino);
        validarNumero(numero, baseOrigen);

        BigInteger decimal = baseADecimal(numero, baseOrigen);
        return decimalABase(decimal, baseDestino);
    }

    // ========================================================================
    // 4. BATERÍA DE PRUEBAS AUTOMÁTICAS
    // ========================================================================

    public static void ejecutarPruebas() {
        System.out.println("=================================================================");
        System.out.println("         EJECUTANDO BATERÍA DE PRUEBAS AUTOMÁTICAS               ");
        System.out.println("=================================================================");

        int aprobadas = 0;
        int total = 0;

        // Caso 1: 101101 (base 2) a base 10 -> 45
        total++;
        if ("45".equals(convertir("101101", 2, 10))) {
            System.out.println("  [✔] Caso 1 superado: 101101 (base 2) = 45 (base 10)");
            aprobadas++;
        }

        // Caso 2: FF (base 16) a base 2 -> 11111111
        total++;
        if ("11111111".equals(convertir("FF", 16, 2))) {
            System.out.println("  [✔] Caso 2 superado: FF (base 16) = 11111111 (base 2)");
            aprobadas++;
        }

        // Caso 2b: Minúsculas: ff (base 16) a base 2 -> 11111111
        total++;
        if ("11111111".equals(convertir("ff", 16, 2))) {
            System.out.println("  [✔] Caso 2b (minúsculas) superado: ff (base 16) = 11111111 (base 2)");
            aprobadas++;
        }

        // Caso 3: 89 (base 8) a base 10 -> Error
        total++;
        try {
            convertir("89", 8, 10);
            System.err.println("  [❌] Caso 3 falló: debió rechazar el número 89 en base 8.");
        } catch (IllegalArgumentException e) {
            System.out.println("  [✔] Caso 3 superado: detectó error en base 8 -> " + e.getMessage());
            aprobadas++;
        }

        // Caso 4: Base inválida (< 2)
        total++;
        try {
            convertir("10", 1, 10);
        } catch (IllegalArgumentException e) {
            System.out.println("  [✔] Caso 4 superado: rechazó base menor a 2.");
            aprobadas++;
        }

        // Caso 5: Base inválida (> 36)
        total++;
        try {
            convertir("10", 10, 37);
        } catch (IllegalArgumentException e) {
            System.out.println("  [✔] Caso 5 superado: rechazó base mayor a 36.");
            aprobadas++;
        }

        // Caso 6: Cero
        total++;
        if ("0".equals(convertir("0", 10, 16))) {
            System.out.println("  [✔] Caso 6 superado: 0 (base 10) = 0 (base 16)");
            aprobadas++;
        }

        // Caso 7: Base máxima (36) -> Z = 35
        total++;
        if ("35".equals(convertir("Z", 36, 10))) {
            System.out.println("  [✔] Caso 7 superado: Z (base 36) = 35 (base 10)");
            aprobadas++;
        }

        System.out.println("-----------------------------------------------------------------");
        System.out.printf("  Resumen: %d / %d pruebas superadas satisfactoriamente.%n", aprobadas, total);
        System.out.println("=================================================================\n");
    }

    // ========================================================================
    // 5. INTERFAZ DE USUARIO Y CICLO PRINCIPAL
    // ========================================================================

    public static void main(String[] args) {
        // Si se pasa el argumento 'test', ejecutar directamente las pruebas
        if (args.length > 0 && (args[0].equalsIgnoreCase("--test") || args[0].equalsIgnoreCase("test"))) {
            ejecutarPruebas();
            return;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("=================================================================");
        System.out.println("        CONVERSOR ENTRE SISTEMAS DE NUMERACIÓN (BASES 2 - 36)    ");
        System.out.println("           Arquitectura de Computadoras - Deber Semana 2         ");
        System.out.println("=================================================================");
        System.out.println("Conversión manual: Horner (a decimal) y Divisiones Sucesivas.");
        System.out.println("Letras válidas: A = 10, B = 11, ..., Z = 35.");
        System.out.println("Tip: Escriba 'test' para ejecutar las pruebas automáticas.\n");

        boolean continuar = true;
        while (continuar) {
            try {
                System.out.print("➤ Ingrese el número a convertir: ");
                String numero = scanner.nextLine().trim();

                if (numero.equalsIgnoreCase("test") || numero.equalsIgnoreCase("t")) {
                    ejecutarPruebas();
                } else {
                    int baseOrigen = leerEntero(scanner, "➤ Ingrese la base de origen (2 - 36): ");
                    int baseDestino = leerEntero(scanner, "➤ Ingrese la base de destino (2 - 36): ");

                    validarBase(baseOrigen);
                    validarBase(baseDestino);
                    validarNumero(numero, baseOrigen);

                    BigInteger decimalPuente = baseADecimal(numero, baseOrigen);
                    String resultadoFinal = decimalABase(decimalPuente, baseDestino);

                    System.out.println("\n-----------------------------------------------------------------");
                    System.out.println("                    RESULTADO DE LA CONVERSIÓN                  ");
                    System.out.println("-----------------------------------------------------------------");
                    System.out.printf("  Número original      : %s (base %d)%n", numero.toUpperCase(), baseOrigen);
                    System.out.printf("  Valor decimal puente : %s (base 10)%n", decimalPuente.toString());
                    System.out.printf("  Equivalente final    : %s (base %d)%n", resultadoFinal, baseDestino);
                    System.out.println("-----------------------------------------------------------------");
                    System.out.printf("  Resumen: %s (base %d) = %s (base %d)%n",
                            numero.toUpperCase(), baseOrigen, resultadoFinal, baseDestino);
                    System.out.println("-----------------------------------------------------------------\n");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("\n[ERROR DE VALIDACIÓN]");
                System.out.println(">> " + e.getMessage() + "\n");
            } catch (Exception e) {
                System.out.println("\n[ERROR INESPERADO] " + e.getMessage() + "\n");
            }

            continuar = preguntarContinuar(scanner);
        }

        System.out.println("\nPrograma finalizado. ¡Hasta pronto!");
        scanner.close();
    }

    private static int leerEntero(Scanner scanner, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String entrada = scanner.nextLine().trim();
            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("  [!] Error: Ingrese un número entero válido (ej. 2, 8, 10, 16).\n");
            }
        }
    }

    private static boolean preguntarContinuar(Scanner scanner) {
        while (true) {
            System.out.print("¿Desea realizar otra conversión? (s/n): ");
            String respuesta = scanner.nextLine().trim().toLowerCase();
            if (respuesta.equals("s") || respuesta.equals("si") || respuesta.equals("sí") || respuesta.equals("y")) {
                System.out.println();
                return true;
            } else if (respuesta.equals("n") || respuesta.equals("no")) {
                return false;
            } else {
                System.out.println("Por favor responda 's' para continuar o 'n' para salir.");
            }
        }
    }
}
