package com.parser;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

/**
 * Servlet implementation class Pars
 */
@WebServlet(urlPatterns = "/Parse", description = "Simple parser by Bychek Anton")
public class Pars extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
       

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//ввыставляем кодировку символов и тип контента
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;");
		//принимаем параметр с формы текстового поля
		final String parserParam = request.getParameter("urlParse");
		
		//принимаем параметр с формы где задается какой тег парсим
		final String parsTag = request.getParameter("whatTag");
		
		//вызываем ответы клиенту с сервера
		final PrintWriter out = response.getWriter();
		
		//берем наш checkbox
		final String toText = request.getParameter("textOrHtml");
		
		//take another check box
		final String withOutHtml = request.getParameter("only_text");
		
		//добавляем вывод в файлик
		final String fileName = "/usr/local/eclipse/workspace/Parser/WebContent/outPutFile/output.html";
		
		
		/***
		 * подключаемся к Jsoup и парсим введеный параметр на клиенте
		 * и все это выкладываем в файлик
		 */
		
        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
        	//конектимся
            final Document doc = Jsoup.connect("http://" + parserParam).get();
            //добавляем в список из элементов все что лежит между тегами 
            Elements elements = doc.select(parsTag);
            
            
            //записываем в файлик
            writer.write(doc.outerHtml());
            
           
            /***Если стоит галка - текст
             *  выводим на клиента все что спарсилось в плэйн тексте
             *  иначе, выводим просто в хтмл
             *  eсли стоит галка на  withoutHTML то убираем все теги и оставляем только текст
             * 	*/
            if (toText != null){
            	out.print("<plaintext>" + elements + "</plaintext>");

            }
            else if (withOutHtml != null) {
            	//System.out.println();
            	out.print(elements.text());  
            }
            else {
            	out.print(elements);
            }
        }
        //перехватываем исключение если че
        catch(Exception e){
        	e.printStackTrace();
        	out.print("ERROR, possible somthing wrong with your query");
        }
        //закрываем поток вывода
		out.close();
	 
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		super.doGet(request, response);
	}

}
