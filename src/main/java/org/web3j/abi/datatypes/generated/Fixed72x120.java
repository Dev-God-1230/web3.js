package org.web3j.abi.datatypes.generated;

import java.math.BigInteger;

import org.web3j.abi.datatypes.Fixed;

/**
 * <p>Auto generated code.<br>
 * <strong>Do not modifiy!</strong><br>
 * Please use {@link org.web3j.codegen.AbiTypesGenerator} to update.</p>
 */
public class Fixed72x120 extends Fixed {
  public static final Fixed72x120 DEFAULT = new Fixed72x120(BigInteger.ZERO);

  public Fixed72x120(BigInteger value) {
    super(72, 120, value);
  }

  public Fixed72x120(int mBitSize, int nBitSize, BigInteger m, BigInteger n) {
    super(72, 120, m, n);
  }
}
