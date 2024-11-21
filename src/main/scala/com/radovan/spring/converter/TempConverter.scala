package com.radovan.spring.converter

import com.radovan.spring.dto.{CartDto, CartItemDto, CustomerDto, OrderAddressDto, OrderDto, OrderItemDto, PizzaDto, PizzaSizeDto, RoleDto, ShippingAddressDto, UserDto}
import com.radovan.spring.entity.{CartEntity, CartItemEntity, CustomerEntity, OrderAddressEntity, OrderEntity, OrderItemEntity, PizzaEntity, PizzaSizeEntity, RoleEntity, ShippingAddressEntity, UserEntity}

trait TempConverter {

  def cartEntityToDto(cart: CartEntity): CartDto

  def cartDtoToEntity(cart: CartDto): CartEntity

  def cartItemEntityToDto(cartItem: CartItemEntity): CartItemDto

  def cartItemDtoToEntity(cartItem: CartItemDto): CartItemEntity

  def customerEntityToDto(customer: CustomerEntity): CustomerDto

  def customerDtoToEntity(customer: CustomerDto): CustomerEntity

  def shippingAddressEntityToDto(address: ShippingAddressEntity): ShippingAddressDto

  def shippingAddressDtoToEntity(address: ShippingAddressDto): ShippingAddressEntity

  def roleEntityToDto(role: RoleEntity): RoleDto

  def roleDtoToEntity(role: RoleDto): RoleEntity

  def userEntityToDto(user: UserEntity): UserDto

  def userDtoToEntity(user: UserDto): UserEntity

  def pizzaEntityToDto(pizza: PizzaEntity): PizzaDto

  def pizzaDtoToEntity(pizza: PizzaDto): PizzaEntity

  def pizzaSizeEntityToDto(pizzaSize: PizzaSizeEntity): PizzaSizeDto

  def pizzaSizeDtoToEntity(pizzaSize: PizzaSizeDto): PizzaSizeEntity

  def orderEntityToDto(order: OrderEntity): OrderDto

  def orderDtoToEntity(order: OrderDto): OrderEntity

  def orderItemEntityToDto(orderItem: OrderItemEntity): OrderItemDto

  def orderItemDtoToEntity(orderItem: OrderItemDto): OrderItemEntity

  def orderAddressEntityToDto(address: OrderAddressEntity): OrderAddressDto

  def orderAddressDtoToEntity(address: OrderAddressDto): OrderAddressEntity

  def shippingAddressToOrderAddress(address:ShippingAddressDto):OrderAddressDto

  def cartItemToOrderItem(cartItem:CartItemDto):OrderItemDto
}
