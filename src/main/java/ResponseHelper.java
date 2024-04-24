
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

//writes out correct responses to the frontend

public class ResponseHelper{
	
	
	public static void writeResponse(Object data, String statusString, int httpCode, HttpServletResponse response) throws ServletException, IOException
    {
        JSONObject jsonr = JSONUtil.createObj();
        jsonr.set("status", statusString);
        jsonr.set("data", data);
        response.setContentType("application/json;charset=utf-8");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(httpCode);
        try (PrintWriter out = response.getWriter())
        {
            jsonr.write(out);
            out.flush();
        }

    }
	
	public static void writeOK(Object data, HttpServletResponse response) throws ServletException, IOException{
		
		writeResponse(data, "OK", 200, response);
		
	}
}