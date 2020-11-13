import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

public class ComparingArrays {

    public static void main(String... args) {
        // equals
        String[] planes1 = new String[] { "A320", "B738", "A321", "A319", "B77W", "B737", "A333", "A332" };
        String[] planes2 = new String[] { "A320", "B738", "A321", "A319", "B77W", "B737", "A333", "A332" };

        System.out.println("equals " + Arrays.equals(planes1, planes2)); // true

        // deep equals
        Plane[][] planes3 = new Plane[][] { new Plane[] { new Plane("Plane 1", "A320") },
                new Plane[] { new Plane("Plane 2", "B738") } };
        Plane[][] planes4 = new Plane[][] { new Plane[] { new Plane("Plane 1", "A320") },
                new Plane[] { new Plane("Plane 2", "B738") } };

        System.out.println("deepEquals " + Arrays.deepEquals(planes3, planes4)); // true

        Plane[][] planes5 = new Plane[][] { new Plane[] { new Plane("Plane 1", "A320") },
                new Plane[] { new Plane("Plane 2", "B738") } };
        Plane[][] planes6 = new Plane[][] { new Plane[] { new Plane("Plane 2", "B738") },
                new Plane[] { new Plane("Plane 1", "A320") } };

        System.out.println("deepEquals " + Arrays.deepEquals(planes5, planes6)); // false

        // compare
        int[] i1 = {2, 4, 6, 8, 10};
        int[] i2 = {2, 4, 6, 8, 10};
        int[] i3 = {2, 4, 12, 8, 10};
        
        // Comparing arrays lexicographically
        // Returns: the value 0 if the first and second array are equal and contain the same elements in the same order; a value less than 0 if the first array is
        // lexicographically less than the second array; and a value greater than 0 if the first array is lexicographically greater than the second array
        System.out.println("\nArrays.compare(i1, i2): " + Arrays.compare(i1, i2));
        System.out.println("Arrays.compare(i1, i3): " + Arrays.compare(i1, i3));
        System.out.println("Arrays.compare(i3, i1): " + Arrays.compare(i3, i1));
        
        // Comparing slices of arrays lexicographically
        System.out.println("\nArrays.compare(i1, 0, 3, i3, 0, 3): " +
                Arrays.compare(i1, 0, 3, i3, 0, 3));
        System.out.println("Arrays.compare(i1, 0, 3, i2, 0, 3): " +
                Arrays.compare(i1, 0, 3, i2, 0, 3));
        System.out.println("Arrays.compare(i3, 0, 3, i1, 0, 3): " +
                Arrays.compare(i3, 0, 3, i1, 0, 3));
        
    }

    static class Plane {
        private String name;
        private String model;

        Plane(String name, String model) {
            this.name = name;
            this.model = model;
        }

        String getModel() { return model; }
        String getName() { return name; }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            Plane plane = (Plane) o;
            return Objects.equals(name, plane.name) && Objects.equals(model, plane.model);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, model);
        }
    }
}
