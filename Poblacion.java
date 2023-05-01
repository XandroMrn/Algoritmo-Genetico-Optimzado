package ag;

public class Poblacion {

    Individuo[] poblacion;
    int generacion = 0;

    /*CONSTRUCTOR*/
    public Poblacion(Individuo individuo) {
        Clonacion(individuo);
    }

    public void Clonacion(Individuo individuo) {
        poblacion = new Individuo[100];
        poblacion[0] = individuo.clonar();

        for (int i = 1; i < poblacion.length; i++) {
            poblacion[i] = individuo.clonar();
            poblacion[i].Mutacion();
        }
    }

    public void eliminarInadaptados() {
        int j = 0;
        for (int i = 0; i < poblacion.length; i++) {
            j = i - 1;
            Individuo temp = poblacion[i];
            while (j >= 0 && temp.calificarIndividuo() > poblacion[j].calificarIndividuo()) {
                poblacion[j + 1] = poblacion[j];
                j--;
            }
            poblacion[j + 1] = temp;
        }
        
        /*Se obtienen los adaptados*/
        Individuo[] adaptados = new Individuo[poblacion.length / 2];

        for (int i = 0; i < poblacion.length / 2; i++) {
            adaptados[i] = poblacion[i];
        }
        poblacion = adaptados;
    }

    public void Cruzamiento() {
        Individuo[] descendencia = new Individuo[poblacion.length * 2];
        int descendientes = 0;

        int numParejas = poblacion.length / 2;
        
        descendencia[0] = poblacion[0]; // no mata al mejor individuo
        
        /*Seleccionar parejas*/
        for (int i = 0; i < numParejas; i++) {
            int padre = (int) (Math.random() * poblacion.length); // Indice para el padre
            int madre = (int) (Math.random() * poblacion.length); // //Indice para la madre

            while (padre == madre) {
                padre = (int) (Math.random() * poblacion.length);
                madre = (int) (Math.random() * poblacion.length);
            }
            /*Crear hijos*/
            if (descendientes >= 1) { // Ayuda a que no se reduzca la calificacion
                descendencia[descendientes] = getHijo(padre, madre, padre);
            }            
            descendientes++;
            descendencia[descendientes] = getHijo(madre, padre, madre);
            descendientes++;
            descendencia[descendientes] = getHijo(padre, padre, madre);
            descendientes++;
            descendencia[descendientes] = getHijo(madre, madre, padre);
            descendientes++;

            Individuo[] cruzados = new Individuo[poblacion.length - 2];
            int indiceCruzados = 0;

            for (int j = 0; j < poblacion.length; j++) {
                if (j != padre && j != madre) {
                    cruzados[indiceCruzados] = poblacion[j];
                    indiceCruzados++;
                }
            }
            poblacion = cruzados;
        }        
        poblacion = descendencia; // Nueva generación
        generacion++;

        // Mutar a los descendientes
        for (int i = 1; i < poblacion.length; i++) {
            poblacion[i].Mutacion();
        }
    }

    public Individuo getHijo(int i1, int i2, int i3) {
        Individuo hijo;
        String[] secuenciaHijo = new String[poblacion[i1].secuencia.length];
        int letrasSecuencia = 0;
        int letrasCorrectas = 0;
        int condicion = 0;
        for (int i = 0; i < poblacion[i1].secuencia.length; i++) { // Recorre las filas
            letrasCorrectas = 0;
            for (int j = 0; j < poblacion[i1].secuencia[i].length(); j++) {
                if (Character.isLetter(poblacion[i1].secuencia[i].charAt(j))) {
                    letrasCorrectas++;
                }
            }
            secuenciaHijo[i] = "";
            int division1;
            int division2;
            division1 = poblacion[i1].secuencia[i].length() / 3;
            division2 = 2 * division1;
            letrasSecuencia = 0;
            for (int j = 0; j < division1; j++) { // Recorre las columnas
                secuenciaHijo[i] = secuenciaHijo[i] + poblacion[i1].secuencia[i].charAt(j);

                if (Character.isLetter(poblacion[i1].secuencia[i].charAt(j))) {
                    letrasSecuencia++;
                }
            }

            int letrasSecuencia2 = 0;
            for (int j = 0; j < division2; j++) {
                if (letrasSecuencia2 >= letrasSecuencia) {
                    secuenciaHijo[i] = secuenciaHijo[i] + poblacion[i2].secuencia[i].charAt(j);

                    if (Character.isLetter(poblacion[i2].secuencia[i].charAt(j))) {
                        letrasSecuencia2++;
                    }
                } else {
                    if (Character.isLetter(poblacion[i2].secuencia[i].charAt(j))) {
                        letrasSecuencia2++;
                    }
                }
            }

            if (letrasSecuencia2 < letrasSecuencia) {
                letrasSecuencia2 = letrasSecuencia;
            }

            letrasSecuencia = 0;
            for (int j = 0; j < poblacion[i3].secuencia[i].length(); j++) {
                if (letrasSecuencia >= letrasSecuencia2) {
                    secuenciaHijo[i] = secuenciaHijo[i] + poblacion[i3].secuencia[i].charAt(j);

                    if (Character.isLetter(poblacion[i3].secuencia[i].charAt(j))) {
                        letrasSecuencia++;
                    }
                } else {
                    if (Character.isLetter(poblacion[i3].secuencia[i].charAt(j))) {
                        letrasSecuencia++;
                    }
                }
            }

            if (condicion == 1) {
                System.out.print(poblacion[i1].secuencia[i] + " " + poblacion[i2].secuencia[i] + "  " + poblacion[i3].secuencia[i]);
                System.out.print(division1 + "  " + division2 + "   " + secuenciaHijo[i]);
            }

            int letrasSecuenciaHijo = 0;
            for (int j = 0; j < secuenciaHijo[i].length(); j++) {
                if (Character.isLetter(secuenciaHijo[i].charAt(j))) {
                    letrasSecuenciaHijo++;
                }
            }
            if (letrasSecuenciaHijo != letrasCorrectas) {
                if (condicion == 0) {
                    condicion = 1;
                } else if (condicion == 1) {
                    condicion = 2;
                }
                i--;
            }
        }
        hijo = new Individuo(secuenciaHijo);
        return hijo;
    }

    public Individuo mejorAdaptado() {
        Individuo evolucionado = null;
        int puntuacion = Integer.MIN_VALUE;

        for (int i = 0; i < poblacion.length; i++) {
            if (poblacion[i].calificarIndividuo() > puntuacion) {
                puntuacion = poblacion[i].calificarIndividuo();
                evolucionado = poblacion[i];
            }
        }
        return evolucionado;
    }
}
