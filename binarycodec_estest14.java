// Source: org.apache.commons.codec.binary.BinaryCodec

@Override
public Object encode(final Object pObject) throws EncoderException {
    if (!(pObject instanceof byte[])) {
        throw new EncoderException("argument not a byte array");
    }
    return toAsciiChars((byte[]) pObject);
}