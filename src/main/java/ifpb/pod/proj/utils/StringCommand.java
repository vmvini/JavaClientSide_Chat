package ifpb.pod.proj.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vmvini on 07/05/16.
 */
public class StringCommand {

    public static Map<String, String> convert(String params) {
        Map<String, String> map = new HashMap<String, String>();

        String commandSplit[] = params.split("\\?");
        map.put("command", commandSplit[0]);

        if (commandSplit.length > 1) {
            String[] paramsSpit = commandSplit[1].split("&");
            for (String param : paramsSpit) {
                String[] keyValue = param.split("=");
                map.put(keyValue[0], keyValue[1]);
            }

        }
        return map;
    }
}
