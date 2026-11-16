package br.com.catalogo.controller;

import br.com.catalogo.dao.FilmeDAOJdbc;
import br.com.catalogo.exception.ItemNaoEncontradoException;
import br.com.catalogo.exception.PersistenciaException;
import br.com.catalogo.exception.ValidacaoException;
import br.com.catalogo.model.Filme;
import br.com.catalogo.model.Genero;
import br.com.catalogo.service.FilmeService;
import br.com.catalogo.validation.FilmeValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controlador unico do CRUD (padrao MVC).
 * <p>
 * GET  /filmes                 -> lista (com filtro opcional ?q=)
 * GET  /filmes?acao=novo       -> formulario vazio
 * GET  /filmes?acao=editar&id= -> formulario preenchido
 * POST /filmes?acao=salvar     -> inclui/atualiza
 * POST /filmes?acao=excluir    -> exclui
 * <p>
 * Toda excecao e tratada aqui: erros de validacao voltam ao formulario com
 * mensagens por campo; erros tecnicos sao registrados em log e o usuario
 * recebe apenas uma mensagem amigavel.
 */
@WebServlet("/filmes")
public class FilmeServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(FilmeServlet.class.getName());
    private static final String VIEW_LISTA = "/WEB-INF/views/lista.jsp";
    private static final String VIEW_FORM = "/WEB-INF/views/form.jsp";
    private static final String VIEW_ERRO = "/WEB-INF/views/erro.jsp";

    private FilmeService service;

    @Override
    public void init() {
        service = new FilmeService(new FilmeDAOJdbc());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String acao = req.getParameter("acao");
        try {
            if ("novo".equals(acao)) {
                abrirFormulario(req, resp, new Filme());
            } else if ("editar".equals(acao)) {
                abrirFormulario(req, resp, service.buscar(lerId(req)));
            } else {
                req.setAttribute("filmes", service.listar(req.getParameter("q")));
                req.setAttribute("q", req.getParameter("q"));
                req.getRequestDispatcher(VIEW_LISTA).forward(req, resp);
            }
        } catch (ItemNaoEncontradoException | NumberFormatException e) {
            erro(req, resp, HttpServletResponse.SC_NOT_FOUND, "O filme solicitado nao foi encontrado.", e);
        } catch (PersistenciaException e) {
            erro(req, resp, HttpServletResponse.SC_SERVICE_UNAVAILABLE, e.getMessage()
                    + " Tente novamente em instantes.", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String acao = req.getParameter("acao");
        try {
            if ("excluir".equals(acao)) {
                service.excluir(lerId(req));
                redirecionar(req, resp, "Filme excluido com sucesso.");
            } else {
                Filme f = FilmeValidator.validar(
                        req.getParameter("id"), req.getParameter("titulo"),
                        req.getParameter("diretor"), req.getParameter("ano"),
                        req.getParameter("genero"), req.getParameter("duracao"),
                        req.getParameter("nota"), req.getParameter("sinopse"));
                boolean novo = f.getId() == null;
                service.salvar(f);
                redirecionar(req, resp, novo ? "Filme cadastrado com sucesso." : "Filme atualizado com sucesso.");
            }
        } catch (ValidacaoException e) {
            // Devolve o formulario com o que o usuario digitou + mensagens por campo
            req.setAttribute("erros", e.getErros());
            req.setAttribute("generos", Genero.values());
            req.setAttribute("valores", new ValoresFormulario(req));
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            req.getRequestDispatcher(VIEW_FORM).forward(req, resp);
        } catch (ItemNaoEncontradoException | NumberFormatException e) {
            erro(req, resp, HttpServletResponse.SC_NOT_FOUND, "O filme solicitado nao foi encontrado.", e);
        } catch (PersistenciaException e) {
            erro(req, resp, HttpServletResponse.SC_SERVICE_UNAVAILABLE, e.getMessage()
                    + " Tente novamente em instantes.", e);
        }
    }

    private void abrirFormulario(HttpServletRequest req, HttpServletResponse resp, Filme f)
            throws ServletException, IOException {
        req.setAttribute("valores", ValoresFormulario.de(f));
        req.setAttribute("generos", Genero.values());
        req.getRequestDispatcher(VIEW_FORM).forward(req, resp);
    }

    /** Padrao Post-Redirect-Get: evita reenvio do formulario ao atualizar a pagina. */
    private void redirecionar(HttpServletRequest req, HttpServletResponse resp, String msg) throws IOException {
        req.getSession().setAttribute("mensagem", msg);
        resp.sendRedirect(req.getContextPath() + "/filmes");
    }

    private int lerId(HttpServletRequest req) {
        return Integer.parseInt(req.getParameter("id"));
    }

    private void erro(HttpServletRequest req, HttpServletResponse resp, int status, String msg, Exception e)
            throws ServletException, IOException {
        LOG.log(Level.WARNING, msg, e); // detalhe tecnico apenas no log do servidor
        resp.setStatus(status);
        req.setAttribute("mensagemErro", msg);
        req.getRequestDispatcher(VIEW_ERRO).forward(req, resp);
    }
}
