// 

// 

package org.aestia.login.packet;

import org.aestia.object.Account;
import org.aestia.kernel.Main;
import org.aestia.login.LoginClient;

public class ChooseNickName
{
    public static void verify(final LoginClient client, final String nickname) {
        final Account account = client.getAccount();
        if (!account.getPseudo().isEmpty()) {
            client.kick();
            return;
        }
        if (nickname.toLowerCase().equals(account.getName().toLowerCase())) {
            client.send("AlEr");
            return;
        }
        final String[] s = { "admin", "modo", " ", "&", "\u00e9", "\"", "'", "(", "-", "\u00e8", "_", "\u00e7", "\u00e0", ")", "=", "~", "#", "{", "[", "|", "`", "^", "@", "]", "}", "°", "+", "^", "$", "\u00f9", "*", ",", ";", ":", "!", "<", ">", "¨", "£", "%", "µ", "?", ".", "/", "§", "\n" };
        for (int i = 0; i < s.length; ++i) {
            if (nickname.contains(s[i])) {
                client.send("AlEs");
                break;
            }
        }
        if (Main.database.getAccountData().exist(nickname) != null) {
            client.send("AlEs");
            return;
        }
        client.getAccount().setPseudo(nickname);
        client.setStatus(LoginClient.Status.SERVER);
        client.getAccount().setState(0);
        AccountQueue.verify(client);
    }
}
