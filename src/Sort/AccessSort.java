package Sort;

import Lifecycle.Lifecycle;
import Lifecycle.LifecycleFactory;
import Rules.AccessAuthority.AccessAuthority;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by armand on 3/1/15.
 */

/**
 * This class determines if the current class is an Activity or a Fragment
 * and sorts the lifecycle methods based off that criteria.
 */
public class AccessSort {

    private PsiClass mPsiClass;

    public AccessSort(PsiClass psiClass) {
        mPsiClass = psiClass;
    }

    /**
     * Formats the activity/fragment lifecycle methods
     */
    public void sort() {
        Map<String, PsiMethod> methods = getMethodsMap();

        Map<String, List<PsiMethod>> sortedMethods = null;

/*        LifecycleFactory lifecycleFactory = new LifecycleFactory();
        Lifecycle lifecycle = lifecycleFactory.createLifecycle(mPsiClass, methods);*/
        AccessAuthority accessAuthority = new AccessAuthority(mPsiClass);
        if (accessAuthority != null && !methods.isEmpty()) {
            sortedMethods = accessAuthority.sort();

            List<PsiMethod> privateMethods = sortedMethods.get("PRIVATE");
            System.out.print(""+(null!=privateMethods?privateMethods.size():"empty"));
            appendSortedMethods(privateMethods);
            deleteUnsortedLifecycleMethods(privateMethods);

            List<PsiMethod> defaultMethods = sortedMethods.get("DEFAULT");
            System.out.print(""+(null!=defaultMethods?defaultMethods.size():"empty"));
            appendSortedMethods(defaultMethods);
            deleteUnsortedLifecycleMethods(defaultMethods);

            List<PsiMethod> protectedMethods = sortedMethods.get("PROTECTED");
            System.out.print(""+(null!=protectedMethods?protectedMethods.size():"empty"));
            appendSortedMethods(protectedMethods);
            deleteUnsortedLifecycleMethods(protectedMethods);

            List<PsiMethod> publicMethods = sortedMethods.get("PUBLIC");
            System.out.print(""+(null!=publicMethods?publicMethods.size():"empty"));
            appendSortedMethods(publicMethods);
            deleteUnsortedLifecycleMethods(publicMethods);
        }

    }

    /**
     * Generates a Map of all the methods in the current class
     * @return a Map of all the methods in the current class
     */
    @NotNull
    private Map<String, PsiMethod> getMethodsMap() {
        StringBuffer buffer = new StringBuffer();
        PsiMethod[] psiClassMethods = mPsiClass.getMethods();
//        if(null != psiClassMethods){
//            for(int i=0,size = psiClassMethods.length;i<size;i++){
//                PsiMethod method = psiClassMethods[i];
//                PsiModifierList modifierList =   method.getModifierList();
//                System.out.print("method:"+method+"  modifierList:"+modifierList);
//                buffer.append("method:"+method+"  modifierList:"+modifierList+"\n");
//                PsiElement[] psiElements =  modifierList.getChildren();
//                if(psiElements!=null){
//                    for(int j=0;j<psiElements.length;j++){
//                        System.out.print("psiElement:"+psiElements[j]);
//                        buffer.append("psiElement:"+psiElements[j]+"\n");
//                    }
//                }
//                JvmModifier[] jvmModifiers = method.getModifiers();JvmModifier
//                if(null!=jvmModifiers){
//                    for(int k=0;k<jvmModifiers.length;k++){
//                        System.out.print("jvmModifier:"+jvmModifiers[k]);
//                        buffer.append("jvmModifier:"+jvmModifiers[k]+"\n");
//                    }
//                }
//            }
//        }

//        Messages.showMessageDialog(buffer.toString(),"thiTitle",Messages.getInformationIcon());
        Map<String, PsiMethod> methods = new LinkedHashMap<String, PsiMethod>();

        for (PsiMethod method : psiClassMethods) {
            if (method != null) {
                methods.put(method.getName(), method);
            }
        }
        return methods;
    }


    /**
     * Removes the collection of PsiMethods from the PsiClass
     *
     * @param methods the methods to remove from the PsiClass
     */
    private void deleteUnsortedLifecycleMethods(Collection<PsiMethod> methods) {
        for (PsiMethod method : methods) method.delete();
    }


    /**
     * Appends the sorted methods to the file
     *
     * @param sortedMethods The sorted methods to append
     */
    private void appendSortedMethods(List<PsiMethod> sortedMethods) {
        appendToStart(sortedMethods);
    }

    /**
     * Appends the sorted lifecycle methods to the end of the class.
     * @param sortedMethods The sorted lifecycle methods
     */
    private void appendToEnd(Map<String, PsiMethod> sortedMethods) {
        for (PsiMethod method : sortedMethods.values()) {
            mPsiClass.add(method);
        }
    }

    /**
     * Appends the sorted lifecycle methods to the start of the class.
     * @param sortedMethods The sorted lifecycle methods
     */
    private void appendToStart(List<PsiMethod> sortedMethods) {
        // We want the lifecycle methods to be the first methods in the class
        // so we grab the current first method and append the lifecycle
        // methods before it.
        PsiElement anchorToAddBefore = mPsiClass.getMethods()[0];

        for (PsiMethod method : sortedMethods) {
            mPsiClass.addBefore(method, anchorToAddBefore);
        }
    }

}
