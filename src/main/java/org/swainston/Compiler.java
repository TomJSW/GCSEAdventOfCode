package org.swainston;

import javax.tools.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Taken from <a
 * href="https://docs.oracle.com/javase/8/docs/api/javax/tools/JavaCompiler.html">Javadoc</a>
 */
public class Compiler implements Serializable {

  public static Result checkCompiles(Attempt challenge) {
    /**
     * A file object used to represent source coming from a string.
     */
    class JavaSourceFromString extends SimpleJavaFileObject {
      /**
       * The source code of this "file".
       */
      final String code;

      /**
       * Constructs a new JavaSourceFromString.
       *  @param name the name of the compilation unit represented by this file object
       *  @param code the source code for the compilation unit represented by this file object
       */
      JavaSourceFromString(String name, String code) {
        super(URI.create(
                String.format("string:///%s%s", name.replace('.', '/'), Kind.SOURCE.extension)),
            Kind.SOURCE);
        this.code = code;
      }

      @Override
      public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        return code;
      }
    }

    Result result = new Result(challenge.getAttempt());
    String submission = result.getSubmission();

    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    DiagnosticCollector<JavaFileObject> diagnosticCollector = new DiagnosticCollector<>();
    JavaSourceFromString source = new JavaSourceFromString("tom", submission); // TODO
    List<JavaSourceFromString> sources = new ArrayList<>();
    sources.add(source);

    List<String> options = List.of("-Xlint:unchecked");
    StandardJavaFileManager mgr = compiler.getStandardFileManager(null, null, null);

    JavaFileManager fileManager = new ForwardingJavaFileManager(mgr) {
      @Override
      public JavaFileObject getJavaFileForOutput(Location location, String className,
                                                 JavaFileObject.Kind kind, FileObject sibling)
          throws IOException {
        JavaFileObject simpleJavaFileObject =
            new SimpleJavaFileObject(URI.create(className), kind) {
              @Override
              public OutputStream openOutputStream() {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                result.setByteArrayOutputStream(byteArrayOutputStream);
                return byteArrayOutputStream;
              }
            };
        return simpleJavaFileObject;
      }
    };


    JavaCompiler.CompilationTask task =
        compiler.getTask(null, fileManager, diagnosticCollector, options, null, sources);
    task.call();
    List<String> errors = new ArrayList<>();
    for (Diagnostic<? extends JavaFileObject> diagnostic : diagnosticCollector.getDiagnostics()) {
      errors.add(String.format("Line: %d, %s in %s", diagnostic.getLineNumber(),
          diagnostic.getMessage(null), diagnostic.getSource().getName()));
    }

    result.setCompiles(errors.isEmpty());
    result.setErrors(errors);

    return result;
  }

  public static class Result {

    private static final String MARKER = "\n===========================";

    private boolean compiles;
    private List<String> errors;
    private String submission;
    private ByteArrayOutputStream byteArrayOutputStream;

    Result(String attempt) {
      int index = attempt.indexOf(MARKER);
      if (index > 0) {
        attempt = attempt.substring(0, index);
      }
      submission = attempt;
    }

    public ByteArrayOutputStream getByteArrayOutputStream() {
      return byteArrayOutputStream;
    }

    public void setByteArrayOutputStream(ByteArrayOutputStream byteArrayOutputStream) {
      this.byteArrayOutputStream = byteArrayOutputStream;
    }

    public String getSubmission() {
      return submission;
    }

    public void setSubmission(String submission) {
      this.submission = submission;
    }

    public boolean isCompiles() {
      return compiles;
    }

    public void setCompiles(boolean compiles) {
      this.compiles = compiles;
    }

    public List<String> getErrors() {
      return errors;
    }

    public void setErrors(List<String> errors) {
      this.errors = errors;
    }

    @Override
    public String toString() {
      return getSubmission() + MARKER + "\n" + getErrors();
    }
  }
}
