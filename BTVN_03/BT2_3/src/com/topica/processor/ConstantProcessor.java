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

    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        this.messager = processingEnv.getMessager();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_1;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement ann : annotations) {
            Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(ann);
            for (Element element : elements) {
            	Set<Modifier> modifiers = element.getModifiers();
                if (!(modifiers.contains(Modifier.FINAL) && modifiers.contains(Modifier.STATIC))) {
                    messager.printMessage(Diagnostic.Kind.ERROR, "Method/field wasn't public and final!", element);
                }
            }
        }
        return true;
    }
}
