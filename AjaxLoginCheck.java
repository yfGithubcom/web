package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import vo.User;
import dao.UserDao;

@WebServlet(urlPatterns="/ajaxLoginCheck.do")
public class AjaxLoginCheck extends HttpServlet {

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//����������������ʽΪutf-8,��ֹ���Ĳ�������
		request.setCharacterEncoding("utf-8");
		//1.���ձ��ĸ�Ԫ�ص�name����ֵ��ȡ���������ֵ
		String userName=request.getParameter("userName");
		String password=request.getParameter("password");
		String vcode=request.getParameter("vcode");
		String autologin=request.getParameter("autologin");
		//2.��ȡHttpSession����
		HttpSession session=request.getSession();
		//ȡ��CreateVerifyImageController�д�ŵ���֤���ַ���
		String saveVcode=(String) session.getAttribute("verify");
		//��ŷ�����Ϣ��Map
		Map<String,Object> map=new HashMap<String,Object>();
		//�Ƚ��������֤���������ɵ���֤���Ƿ���ͬ
		if(!vcode.equalsIgnoreCase(saveVcode)){//��ͬ
			//��map�д�ŷ�������
			map.put("code",1);
			map.put("info","��֤�벻��ȷ��");
		}else{//��֤����ȷ
			UserDao userDao=new UserDao();
			User user=userDao.get(userName);
			if(user==null){//�û���������
				map.put("code",2);
				map.put("info","�û���������");
			}else{//�û�������
				if(!user.getPassword().equals(password)){//���벻��ȷ
					map.put("code",3);
					map.put("info","���벻��ȷ");
				}else{//�û���������ȷ
					//����Ҫ���ݵ����ݴ����session��Χ�У�һ���Ự�׶ε����г��򶼿��Դ��л�ȡ
					session.setAttribute("currentUser", user);
					if("true".equals(request.getParameter("autologin"))){//���¼ѡ��
						Cookie cookie1=new Cookie("userName",userName);
						Cookie cookie2=new Cookie("password",password);
						cookie1.setPath(request.getContextPath());
						cookie2.setPath(request.getContextPath());
						cookie1.setMaxAge(60*60*24*7);
						cookie2.setMaxAge(60*60*24*7);
						response.addCookie(cookie1);
						response.addCookie(cookie2);
					}
					map.put("code",0);
					map.put("info","��¼�ɹ�");
				}
			}
		}
		//���ùȸ��Gson�⽫map��������ת��Ϊjson�ַ���
		String jsonStr=new Gson().toJson(map);
		//�ַ�������ַ���
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out=response.getWriter();
		out.print(jsonStr);
		out.flush();
		out.close();
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
