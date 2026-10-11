# Deber Semana 2: Conversor entre Sistemas de Numeración
**Materia:** Arquitectura de Computadoras  
**Lenguaje:** Java  
**Diseño:** Programación Modular y Responsabilidad Única (SRP)  

---

## 1. Descripción del Proyecto

Este proyecto implementa un conversor de números expresados en cualquier sistema de numeración (bases comprendidas entre **2** y **36**) a cualquier otra base de destino dentro del mismo rango.

Cumple estrictamente con la restricción de **no utilizar funciones de conversión automáticas del lenguaje** (como `Integer.parseInt(s, radix)` o `Integer.toString(n, radix)`), implementando la lógica matemática manual de evaluación polinomial y divisiones sucesivas.

---

## 2. Estructura Modular del Código

El código fue optimizado para evitar sobreingeniería de clases, organizando la solución mediante el principio de **Responsabilidad Única (SRP)** en funciones modulares claras:

| Función / Método | Responsabilidad |
|---|---|
| `validarBase(int base)` | Comprueba que la base esté en el rango permitido $[2, 36]$. |
| `validarNumero(String numero, int base)` | Verifica símbolo por símbolo que los dígitos pertenezcan al alfabeto de la base origen. |
| `caracterAValor(char c)` | Convierte caracteres alfanuméricos a valores numéricos (soporta mayúsculas y minúsculas). |
| `valorACaracter(int valor)` | Convierte valores de 0 a 35 a sus símbolos (`0`-`9`, `A`-`Z`). |
| `baseADecimal(String numero, int baseOrigen)` | **Algoritmo 1:** Convierte de base origen a decimal usando el **Método de Horner**. |
| `decimalABase(BigInteger decimal, int baseDestino)` | **Algoritmo 2:** Convierte de decimal a base destino mediante **Divisiones Sucesivas**. |
| `convertir(String numero, int origen, int destino)` | Orquesta las validaciones y la conversión completa. |
| `ejecutarPruebas()` | Batería de pruebas automatizadas con todos los casos de prueba del enunciado. |
| `main(String[] args)` | Interfaz de consola interactiva con ciclo de repetición (`s/n`). |

---

## 3. Fundamento Matemático de los Algoritmos

### A. Base Origen $\to$ Decimal (Método de Horner)
Dado un número con dígitos $d_{k-1} \dots d_0$ en base $B$:
$$\text{Decimal} = (\dots((d_{k-1} \cdot B + d_{k-2}) \cdot B + \dots) \cdot B + d_0$$
- Itera de izquierda a derecha acumulando en tiempo lineal $\mathcal{O}(n)$, evitando el cálculo repetitivo de potencias.

### B. Decimal $\to$ Base Destino (Divisiones Sucesivas)
Dado un valor decimal $V$ y una base destino $B_{dest}$:
1. En cada iteración se obtiene el residuo: $r = V \pmod{B_{dest}}$ (dígito de menor a mayor significancia).
2. Se actualiza el valor: $V = \lfloor V / B_{dest} \rfloor$.
3. Se repite hasta que $V = 0$.
4. Se invierte la cadena resultante de residuos para obtener el orden correcto.

---

## 4. Archivos del Proyecto

- [`ConversorBasesApp.java`](ConversorBasesApp.java): Programa principal modular e interactivo (incluye pruebas al escribir `test`).
- [`TestConversor.java`](TestConversor.java): Ejecutor directo de la batería de pruebas automáticas.
- [`README.md`](README.md): Documentación del proyecto.

---

## 5. Instrucciones de Compilación y Ejecución

Desde la terminal en la carpeta `ArquitecturaDeComputadoras`:

```bash
# Compilar
javac ConversorBasesApp.java TestConversor.java

# Ejecutar el conversor interactivo
java ConversorBasesApp

# Ejecutar las pruebas automáticas
java TestConversor
```
