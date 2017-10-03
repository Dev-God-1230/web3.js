package org.web3j.abi.datatypes.generated;

import java.math.BigInteger;
import org.web3j.abi.datatypes.Ufixed;

/**
 * Auto generated code.
 * <p><strong>Do not modifiy!</strong>
 * <p>Please use org.web3j.codegen.AbiTypesGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 */
public class Ufixed32x152 extends Ufixed {
    public static final Ufixed32x152 DEFAULT = new Ufixed32x152(BigInteger.ZERO);

    public Ufixed32x152(BigInteger value) {
        super(32, 152, value);
    }

    public Ufixed32x152(int mBitSize, int nBitSize, BigInteger m, BigInteger n) {
        super(32, 152, m, n);
    }
}
