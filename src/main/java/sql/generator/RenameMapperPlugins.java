package sql.generator;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;
import static org.mybatis.generator.internal.util.messages.Messages.getString;

/**
 * @ClassName: RenameJavaMapperPlugins
 * @Description:
 * @Author: yml
 * @CreateDate: 2019/11/22
 * @UpdateUser: yml
 * @UpdateDate: 2019/11/22
 * @Version: 1.0.0
 */
public class RenameMapperPlugins extends PluginAdapter {

    public static final String CHAR = ".I";
    private String javaSearchString;
    private String javaReplaceString;
    private String xmlSearchString;
    private String xmlReplaceString;
    private Pattern javaPattern;
    private Pattern xmlPattern;
    private boolean javaValid;
    private boolean xmlValid;

    @Override
    public boolean validate(List<String> warnings) {

        javaSearchString = properties.getProperty("javaSearchString");
        javaReplaceString = properties.getProperty("javaReplaceString");
        xmlSearchString = properties.getProperty("xmlSearchString");
        xmlReplaceString = properties.getProperty("xmlReplaceString");

        javaValid = stringHasValue(javaSearchString) || stringHasValue(javaReplaceString);

        if (javaValid) {
            javaPattern = Pattern.compile(javaSearchString);
        } else {
            if (!stringHasValue(javaSearchString)) {
                warnings.add(getString("ValidationError.18", "RenameExampleClassPlugin", "javaSearchString"));
            }
            if (!stringHasValue(javaReplaceString)) {
                warnings.add(getString("ValidationError.18", "RenameExampleClassPlugin", "javaReplaceString"));
            }
        }

        xmlValid = stringHasValue(xmlSearchString) || stringHasValue(xmlReplaceString);

        if (xmlValid) {
            xmlPattern = Pattern.compile(xmlSearchString);
        } else {
            if (!stringHasValue(xmlSearchString)) {
                warnings.add(getString("ValidationError.18", "RenameExampleClassPlugin", "xmlSearchString"));
            }
            if (!stringHasValue(xmlReplaceString)) {
                warnings.add(getString("ValidationError.18", "RenameExampleClassPlugin", "xmlReplaceString"));
            }
        }

        return javaValid || xmlValid;
    }

    @Override
    public void initialized(IntrospectedTable introspectedTable) {
        /*处理javaMapper*/
        String javaType = introspectedTable.getMyBatis3JavaMapperType();
        Pattern pattern = Pattern.compile("\\.\\w+Mapper");
        Matcher matcher = pattern.matcher(javaType);
        String suffix = matcher.find()? matcher.group() : "";
        int index = matcher.regionEnd() - suffix.length();
        suffix = CHAR + suffix.substring(1);
        javaType = javaType.substring(0,index) + suffix;
        matcher = javaPattern.matcher(javaType);
        javaType = matcher.replaceAll(javaReplaceString);
        introspectedTable.setMyBatis3JavaMapperType(javaType);

        /*处理xmlMapper*/
        if(xmlValid){
            String xmlType = introspectedTable.getMyBatis3XmlMapperFileName();
            matcher = xmlPattern.matcher(xmlType);
            xmlType = matcher.replaceAll(xmlReplaceString);
            introspectedTable.setMyBatis3XmlMapperFileName(xmlType);
        }
    }
}
