/*************************************************************************
 * tranSMART - translational medicine data mart
 *
 * Copyright 2008-2012 Janssen Research & Development, LLC.
 *
 * This product includes software developed at Janssen Research & Development, LLC.
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License 
 * as published by the Free Software  * Foundation, either version 3 of the License, or (at your option) any later version, along with the following terms:
 * 1.	You may convey a work based on this program in accordance with section 5, provided that you retain the above notices.
 * 2.	You may convey verbatim copies of this program code as you receive it, in any medium, provided that you retain the above notices.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS    * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 ******************************************************************/


import grails.test.ControllerUnitTestCase
import org.codehaus.groovy.grails.commons.ApplicationAttributes
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import org.codehaus.groovy.grails.support.MockApplicationContext
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsWebRequest
import org.springframework.web.context.request.RequestContextHolder

class AnalysisControllerTests extends ControllerUnitTestCase {
    def grailsApplication

    protected void setUp() {
        super.setUp()
        def ctx = (MockApplicationContext) mockRequest.servletContext.getAttribute(ApplicationAttributes.APPLICATION_CONTEXT)
        ctx.registerMockBean(GrailsApplication.APPLICATION_ID, grailsApplication)
        webRequest = new GrailsWebRequest(mockRequest, mockResponse, mockRequest.servletContext)
        RequestContextHolder.setRequestAttributes(webRequest)
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testSomething() {
        SpringSecurityUtils.doWithAuth('guest') {
            controller.request.setParameter('result_instance_id1', '19416')
            controller.request.setParameter('datasetId', 'VCF TEST')
            controller.showVcf()
        }
    }
}
