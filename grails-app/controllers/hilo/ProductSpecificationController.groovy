package hilo

import io.hilo.BaseController

import io.hilo.ProductSpecification
import grails.plugin.springsecurity.annotation.Secured
import io.hilo.Specification
import io.hilo.SpecificationOption


import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder


@Mixin(BaseController)
class ProductSpecificationController {


    MessageSource messageSource
    

    @Secured(['ROLE_ADMIN'])
    def manage(Long id){
        authenticatedAdminProduct { adminAccount, productInstance ->
            def availableSpecifications = []

            productInstance.catalogs.each { catalog ->
                def c = Specification.createCriteria()
                def results = c.list() {
                    catalogs {
                        idEq(catalog.id)
                    }
                }

                results.each(){ specification ->
                    availableSpecifications.push(specification)
                }
            }

            availableSpecifications.unique { a, b ->
                a.id <=> b.id
            }

            [ productInstance: productInstance, availableSpecifications: availableSpecifications ]
        }
    }


    @Secured(['ROLE_ADMIN'])
    def add(Long id){
        authenticatedAdminProduct { adminAccount, productInstance ->
            def specificationOption = SpecificationOption.get(params.optionId)

            if(!specificationOption){
                flash.message = messageSource.getMessage("something.went.wrong.adding.specification", [ productInstance.name ] as Object[], "Default", LocaleContextHolder.locale)
                redirect(action: 'manage', id: productInstance.id)
                return
            }

            def specification = specificationOption.specification

            def productSpecificationsRemove = []
            if(productInstance.productSpecifications){
                productInstance.productSpecifications.each { productSpecification ->
                    if (productSpecification.specificationOption.specification.id == specification.id) {
                        productSpecificationsRemove.push(productSpecification)
                    }
                }
            }

            if(productSpecificationsRemove){
                productSpecificationsRemove.each { productSpecification ->
                    productInstance.removeFromProductSpecifications(productSpecification)
                    productSpecification.delete(flush:true)
                }
            }


            def productSpecification = new ProductSpecification()
            productSpecification.specificationOption = specificationOption
            productSpecification.specification = specification
            productSpecification.product = productInstance
            productSpecification.save(flush:true)

            productInstance.addToProductSpecifications(productSpecification)
            productInstance.save(flush:true)

            flash.message = messageSource.getMessage("successfully.added.product.specification", null, LocaleContextHolder.locale)
            redirect(action: 'manage', id: productInstance.id)

        }
    }



    @Secured(['ROLE_ADMIN'])
    def remove(Long id){
        authenticatedAdminProduct { adminAccount, productInstance ->
            def specificationOption = SpecificationOption.get(params.optionId)

            if(!specificationOption){
                flash.message = messageSource.getMessage("something.went.wrong.adding.specification", [ productInstance.name ] as Object[], "Default", LocaleContextHolder.locale)
                redirect(action: 'manage', id: productInstance.id)
                return
            }

            def removable = []
            if(productInstance.productSpecifications){
                productInstance.productSpecifications.each { productSpecification ->
                    if (productSpecification.specificationOption.id == specificationOption.id) {
                        removable.add(productSpecification)
                    }
                }
            }
            
            if(removable.size() > 0){
                removable.each { removableProductSpecification ->
                    productInstance.removeFromProductSpecifications(removableProductSpecification)
                    removableProductSpecification.delete(flush:true)
                }
            }


            flash.message = messageSource.getMessage("successfully.removed.product.specification", null, LocaleContextHolder.locale)
            redirect(action: 'manage', id: productInstance.id)
        }
    }


}