package ocp.study.part04;

import java.util.Optional;

public class OptionalExample {
    public static void main(String[] args) {

        Optional<String> gender = Optional.of("MALE");
        String answer1 = "Yes";
        String answer2 = null;

        System.out.println("Non-Empty Optional:" + gender);
        System.out.println("Non-Empty Optional: Gender value : " + gender.get());
        System.out.println("Empty Optional: " + Optional.empty());

        System.out.println("ofNullable on Non-Empty Optional: " + Optional.ofNullable(answer1));
        System.out.println("ofNullable on Empty Optional: " + Optional.ofNullable(answer2));

        System.out.println(gender.isPresent());
        gender.ifPresent(x -> System.out.println("yeahhhh"));
        System.out.println(Optional.empty().orElse("poop"));
        try {
        //  Optional.empty().orElseThrow(Exception::new);
            Optional.empty().orElseThrow(() -> new Exception("b0rKed"));
        } catch (Exception e) {
            System.out.println("Caught exception=" + e);
        }

        // java.lang.NullPointerException
        System.out.println("ofNullable on Non-Empty Optional: " + Optional.of(answer2));

    }

}
