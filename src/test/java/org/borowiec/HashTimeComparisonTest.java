package org.borowiec;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.bouncycastle.crypto.digests.MD5Digest;
import org.bouncycastle.crypto.generators.OpenBSDBCrypt;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;

public class HashTimeComparisonTest {

    private static final int NUMBER_OF_ATTEMPTS = 10;

    @Test
    public void compareHashAlgorithms() {
        String password = RandomStringGenerator.generateRandomAlphanumeric(15);

        performHashTest(password, "MD5", this::md5Hash);
        performHashTest(password, "BCrypt^8", (pwd) -> bCryptHash(pwd, 8));
        performHashTest(password, "BCrypt^10", (pwd) -> bCryptHash(pwd, 10));
        performHashTest(password, "BCrypt^12", (pwd) -> bCryptHash(pwd, 12));
    }

    private void performHashTest(String password, String algorithmName, Function<String, Long> hashFunction) {
        long totalHashTime = 0;

        for (int i = 0; i < NUMBER_OF_ATTEMPTS; ++i) {
            totalHashTime += hashFunction.apply(password);
        }

        long averageHashTime = totalHashTime / NUMBER_OF_ATTEMPTS;
        long hashesPerSecond = TimeUnit.SECONDS.toNanos(1) / averageHashTime;

        System.out.println("---------------------------------------------");
        System.out.println(algorithmName + " average hash time = " + averageHashTime + " ns");
        System.out.println(algorithmName + " hashes per second = " + hashesPerSecond);
        System.out.println("---------------------------------------------");
    }

    private long md5Hash(String password) {
        MD5Digest md5Digest = new MD5Digest();

        long start = System.nanoTime();

        md5Digest.reset();
        md5Digest.update(password.getBytes(), 0, password.length());
        byte[] result = new byte[md5Digest.getDigestSize()];
        md5Digest.doFinal(result, 0);
        String md5Hash = Hex.toHexString(result);

        long end = System.nanoTime();

        System.out.println("MD5 hash: " + md5Hash);

        return end - start;
    }

    private long bCryptHash(String password, int logRounds) {
        long start = System.nanoTime();

        String salt = RandomStringGenerator.generateRandomAlphanumeric(16);
        String bCryptHash = OpenBSDBCrypt.generate("2a", password.toCharArray(), salt.getBytes(), logRounds);

        long end = System.nanoTime();

        System.out.println("BCrypt^" + logRounds + " hash: " + bCryptHash);

        return end - start;
    }

}
