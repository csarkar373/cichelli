public class Tester {
    public static void main(String[] args) {
        String [] words = { "calliope", "clio", "erato", "euterpe", "melpomene", "polyhymnia", "terpsichore", "thalia", "urania" };
        Cichelli cichelli = new Cichelli(words);
        cichelli.go();
    }
}
