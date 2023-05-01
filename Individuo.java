package ag;

public class Individuo {

    String[] secuencia;

    /*CONSTRUCTOR*/
    public Individuo(String[] secuencia) {
        this.secuencia = secuencia;
        insertarGaps();
        eliminarGaps();
    }

    /*Para completar espacios sobrantes con gaps*/
    public void insertarGaps() {
        int maximo = 0;

        for (int i = 0; i < secuencia.length; i++) {
            if (secuencia[i].length() > maximo) {
                maximo = secuencia[i].length();
            }
        }

        for (int i = 0; i < secuencia.length; i++) {
            int numGaps = maximo - secuencia[i].length();
            for (int j = 0; j < numGaps; j++) {
                secuencia[i] = secuencia[i] + '-';
            }
        }
    }

    /*Para eliminar columnas de gaps*/
    public void eliminarGaps() {
        for (int i = 0; i < secuencia[0].length(); i++) { // Recorre columnas
            boolean gaps = true;
            for (int j = 0; j < secuencia.length; j++) { // Recorre filas
                if (secuencia[j].charAt(i) != '-') {
                    gaps = false;
                }
            }
            if (gaps) {
                for (int j = 0; j < secuencia.length; j++) {
                    if (i == secuencia[0].length()) {
                        secuencia[j] = secuencia[j].substring(0, i);
                    } else {
                        secuencia[j] = secuencia[j].substring(0, i) + secuencia[j].substring(i + 1);
                    }
                }
                i--;
            }
        }
        int[] indices = new int[secuencia.length];

        for (int i = 0; i < secuencia[0].length() - 1; i++) { // Recorre columnas
            boolean gaps = true;
            for (int j = 0; j < secuencia.length; j++) { // Recorre filas
                if (secuencia[j].charAt(i) != '-' && secuencia[j].charAt(i + 1) != '-') {
                    gaps = false;
                } else if (secuencia[j].charAt(i) == '-') {
                    indices[j] = i;
                } else {
                    indices[j] = i + 1;
                }
            }
            if (gaps) {
                for (int j = 0; j < secuencia.length; j++) {
                    if (indices[j] == secuencia[0].length()) {
                        secuencia[j] = secuencia[j].substring(0, indices[j]);
                    } else {
                        secuencia[j] = secuencia[j].substring(0, indices[j]) + secuencia[j].substring(indices[j] + 1);
                    }
                }
                i--;
            }
        }
    }

    public int calificarIndividuo() {
        int calificacion = 0;
        String repetidas = "";

        for (int i = 0; i < secuencia[0].length(); i++) { // Para recorrer columnas
            repetidas = "";
            for (int j = 0; j < secuencia.length; j++) { // Para recorrer filas
                if (repetidas.contains(secuencia[j].charAt(i) + "")) {
                    calificacion = calificacion + 10;
                } else if (secuencia[j].charAt(i) == '-') {
                    calificacion = calificacion - 1;
                } else {
                    repetidas = repetidas + secuencia[j].charAt(i);
                }
            }
        }
        return calificacion;
    }

    public void Mutacion() {
        String gap = "-";

        for (int i = 0; i < secuencia.length; i++) {
            int indice = (int) (Math.random() * secuencia[i].length());
            secuencia[i] = secuencia[i].substring(0, indice) + gap + secuencia[i].substring(indice);
        }
        eliminarGaps();
    }

    public Individuo clonar() {
        Individuo individuoClon = new Individuo(secuencia.clone());
        return individuoClon;
    }

    @Override
    public String toString() {
        String ordenado = "";
        for (int i = 0; i < secuencia.length; i++) {
            ordenado += secuencia[i] + "\n";
        }
        ordenado += "Calificacion: " + calificarIndividuo();
        return ordenado;
    }
}
