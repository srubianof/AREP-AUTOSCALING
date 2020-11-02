package edu.eci.arep;

import spark.*;

import java.math.BigInteger;
import java.util.HashMap;

import static spark.Spark.*;

/**
 * Spark Prime Numbers Factorization HTTP Handler
 *
 * @author Santiago Rubiano
 * @version November 2, 2020
 */
public class SparkPrimeNumbers {
    public static void main(String[] args) {
        port(getPort());
        get("/:number", ((request, response) -> {
            HashMap<BigInteger, BigInteger> rta = PrimeNumbers.primeBigFactorize(new BigInteger(request.params(":number")));
            System.out.println(rta);
            return rta;
        }));
    }

    /**
     * This method reads the default port as specified by the PORT variable in
     * the environment.
     * <p>
     * Heroku provides the port automatically so you need this to run the
     * project on Heroku.
     *
     * @return the port
     */
    static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4567; //returns default port if heroku-port isn't set (i.e. on localhost)
    }
}
