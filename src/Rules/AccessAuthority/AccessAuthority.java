package Rules.AccessAuthority;

import com.intellij.lang.jvm.JvmModifier;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifierList;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AccessAuthority {
    private Map<String, PsiMethod> mAllMethods;
    protected List<String> mLifecycleOrdering;
    private PsiClass mPsiClass;
    public AccessAuthority(PsiClass psiClass) {
        this.mPsiClass = psiClass;
    }

    public Map<String, List<PsiMethod>> sort() {
        PsiMethod[] psiClassMethods = mPsiClass.getMethods();
        Map<String, List<PsiMethod>> sortedMethods = new LinkedHashMap<>();
        if(null == psiClassMethods) return null;

        List<PsiMethod> defaultList = new ArrayList<>();
        List<PsiMethod> protectedList = new ArrayList<>();
        List<PsiMethod> privateList = new ArrayList<>();
        List<PsiMethod> publicList = new ArrayList<>();
        for(int i=0,length = psiClassMethods.length;i<length;i++){
            PsiMethod psiMethod = psiClassMethods[i];
            JvmModifier[] jvmModifiers =  psiMethod.getModifiers();
            String accessType = "DEFAULT";
            if(null!=jvmModifiers){
                for(int j =0;j<jvmModifiers.length;j++){
//                    if("PUBLIC".equalsIgnoreCase(jvmModifiers[i].name())||"PROTECTED".equalsIgnoreCase(jvmModifiers[i].name())||"PRIVATE".equalsIgnoreCase(jvmModifiers[i].name())){
//                        accessType = jvmModifiers[i].name().toUpperCase();
//                        break;
//                    }
                    if("PUBLIC".equalsIgnoreCase(jvmModifiers[j].name())
                            ||"PROTECTED".equalsIgnoreCase(jvmModifiers[j].name())||"PRIVATE".equalsIgnoreCase(jvmModifiers[j].name())){
                        accessType = jvmModifiers[j].name().toUpperCase();
                        break;
                    }
                }
            }
            if("DEFAULT".equalsIgnoreCase(accessType)){
                defaultList.add(psiMethod);
            }else if("PRIVATE".equalsIgnoreCase(accessType)){
                privateList.add(psiMethod);
            }else if("PROTECTED".equalsIgnoreCase(accessType)){
                protectedList.add(psiMethod);
            }else if("PUBLIC".equalsIgnoreCase(accessType)){
                publicList.add(psiMethod);
            }
        }
        sortedMethods.put("DEFAULT",defaultList);
        sortedMethods.put("PRIVATE",privateList);
        sortedMethods.put("PROTECTED",protectedList);
        sortedMethods.put("PUBLIC",publicList);
        // LinkedHashMap because we must respect the ordering in which we insert

/*        for (int i = 0; i < mLifecycleOrdering.size(); i++) {
            String methodName = mLifecycleOrdering.get(i);
            PsiMethod method = mAllMethods.get(methodName);

            if (method != null) {
                sortedMethods.put(methodName, method);
            }
        }*/

        return sortedMethods;
    }
}
