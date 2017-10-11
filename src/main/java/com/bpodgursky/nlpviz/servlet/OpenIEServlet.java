package com.bpodgursky.nlpviz.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.bpodgursky.nlpviz.OpenIEParser;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpenIEServlet extends HttpServlet {
  private static final Logger LOG = LoggerFactory.getLogger(ParseServlet.class);

  private final OpenIEParser openieParser;

  public OpenIEServlet() throws IOException {
	  openieParser = new OpenIEParser();
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    try {
      String sentence = req.getParameter("text");
      LOG.info("Processing request: "+sentence);

      String lang = req.getParameter("lang");

      if(lang == null || lang.equals("en")) {
        resp.getWriter().append(openieParser.parse(sentence).toString());
      }

    } catch (JSONException e) {
      throw new RuntimeException(e);
    }
  }
}
