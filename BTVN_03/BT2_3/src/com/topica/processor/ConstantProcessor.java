package com.topica.processor;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Set;

@SupportedAnnotationTypes(value = {"com.topica.annotation.Constant"})
@SupportedSourceVersion(SourceVersion.RELEASE_1)
public class ConstantProcessor extends AbstractProcessor {

    private Filer filer;
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_1;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement ann : annotations) {
            Set<? extends Element> e2s = roundEnv.getElementsAnnotatedWith(ann);
            for (Element e2 : e2s) {
                Set<Modifier> modifiers = e2.getModifiers();
                if (!((modifiers.contains(Modifier.STATIC) && (modifiers.contains(Modifier.FINAL))))) {
                    messager.printMessage(Diagnostic.Kind.ERROR,
                            "Field wasn't static and final", e2);

                }
            }
        }
        return true;
    }
}
