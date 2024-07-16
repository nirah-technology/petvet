package io.nirahtech.petvet.simulator.cadastre.domain;

public final class Mathematics {
    private Mathematics() {
    }

    // Fonction pour calculer l'angle intérieur en degrés
    public static double computeInnerAngleToDegrees(double[] a, double[] b, double[] c) {
        // Vecteurs v1 = AB et v2 = BC
        double[] v1 = { a[0] - b[0], a[1] - b[1] };
        double[] v2 = { c[0] - b[0], c[1] - b[1] };

        // Calcul du produit scalaire v1 . v2
        double dotProduct = innerProduct(v1, v2);

        // Calcul des normes ||v1|| et ||v2||
        double normV1 = norm(v1);
        double normV2 = norm(v2);

        // Calcul de l'angle en radians
        double angleRad = Math.acos(dotProduct / (normV1 * normV2));

        // Conversion en degrés
        double angleDeg = Math.toDegrees(angleRad);

        return angleDeg;
    }

    public static double computeOuterAngleToDegrees(double[] a, double[] b, double[] c) {
        // Vecteurs v1 = AB et v2 = BC
        double[] v1 = { a[0] - b[0], a[1] - b[1] };
        double[] v2 = { c[0] - b[0], c[1] - b[1] };
        
        // Calcul du produit scalaire v1 . v2
        double dotProduct = innerProduct(v1, v2);
        
        // Calcul des normes ||v1|| et ||v2||
        double normV1 = norm(v1);
        double normV2 = norm(v2);
        
        // Calcul de l'angle en radians
        double angleRad = Math.acos(dotProduct / (normV1 * normV2));
        
        // Conversion en degrés
        double angleDeg = Math.toDegrees(angleRad);
        
        // Détermination du sens de l'angle en utilisant le produit vectoriel
        double crossProduct = v1[0] * v2[1] - v1[1] * v2[0];
        if (crossProduct < 0) {
            // Angle orienté dans le sens anti-horaire, donc on le complémente à 360 degrés
            angleDeg = 360 - angleDeg;
        }
        
        return angleDeg;
    }


    // Fonction pour calculer le produit scalaire de deux vecteurs
    private static double innerProduct(double[] a, double[] b) {
        double result = 0;
        for (int i = 0; i < a.length; i++) {
            result += a[i] * b[i];
        }
        return result;
    }

    // Fonction pour calculer la norme d'un vecteur
    private static double norm(double[] v) {
        double sum = 0;
        for (double vi : v) {
            sum += vi * vi;
        }
        return Math.sqrt(sum);
    }

    public static final double computeLength(double[] A, double[] B) {
        final double[] AB = { B[0] - A[0], B[1] - A[1] };
        return Math.sqrt(AB[0] * AB[0] + AB[1] * AB[1]);
    }

    public static final double[] computeVector(double[] A, double[] B) {
        return new double[] { A[0] - B[0], A[1] - B[1] };
    }

    public static final double[] computeMiddle(double[] A, double[] B) {
        final double middleX = (A[0] + B[0]) / 2;
        final double middleY = (A[1] + B[1]) / 2;
        return new double[] { middleX, middleY };
    }

    public static final double[] computePoint(double[] A, double[] B, double distanceFromA) {
        // Vecteur AB
        final double[] AB = { B[0] - A[0], B[1] - A[1] };

        // Longueur du segment AB
        final double lengthAB = Math.sqrt(AB[0] * AB[0] + AB[1] * AB[1]);

        // Proportion du vecteur AB nécessaire pour atteindre la distance souhaitée
        final double ratio = distanceFromA / lengthAB;

        // Calcul des coordonnées du point Z
        final double Zx = A[0] + ratio * AB[0];
        final double Zy = A[1] + ratio * AB[1];

        return new double[] { Zx, Zy };
    }
}
