/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chantake.getuuid;

import com.chantake.mituyaapi.tools.DBType;
import com.chantake.mituyaapi.tools.DatabaseConnection;
import com.chantake.mituyaapi.tools.database.DatabaseConnectionManager;
import com.chantake.mituyaapi.tools.database.JDCConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.Security;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import com.mysql.jdbc.Driver;

/**
 *
 * @author chantake
 */
public class Main {

    

    public static void main(String[] args) {
        new GetLinkShellList(args);
        //new UpdateUser(args);
    }

    
}
