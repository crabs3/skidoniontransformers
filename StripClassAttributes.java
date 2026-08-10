import java.nio.file.Files;
import java.nio.file.Path;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

/** fuck you crashing my fucking decompiler you fucking gook cunts */
public final class StripClassAttributes {
    public static void main(String[] args) throws Exception {
        if (args.length != 2) throw new IllegalArgumentException("in out");
        byte[] input = Files.readAllBytes(Path.of(args[0]));
        ClassReader reader = new ClassReader(input);
        ClassWriter writer = new ClassWriter(0);
        ClassVisitor visitor = new ClassVisitor(org.objectweb.asm.Opcodes.ASM9, writer) {
            @Override public void visitAttribute(org.objectweb.asm.Attribute attribute) { }
            @Override public org.objectweb.asm.FieldVisitor visitField(int access, String name, String descriptor,
                    String signature, Object value) {
                return super.visitField(access, name, descriptor, null, value);
            }
            @Override public org.objectweb.asm.MethodVisitor visitMethod(int access, String name, String descriptor,
                    String signature, String[] exceptions) {
                return super.visitMethod(access, name, descriptor, null, exceptions);
            }
            @Override public void visit(int version, int access, String name, String signature, String superName,
                    String[] interfaces) {
                super.visit(version, access, name, null, superName, interfaces);
            }
        };
        reader.accept(visitor, 0);
        Files.write(Path.of(args[1]), writer.toByteArray());
    }
}
