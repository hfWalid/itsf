# Recruitment test

## A Subscription management system

ITSF need to rework its subscription management system. We need you to init a project with some very basic features :

We need to persist some information :
- type of subscription (FIX, MOBILE, FIBER), date of subscription, id client
- a subscription can have one or more options : name (ROAMING, NETFLIX) and a date of subscription.

To manage those subscriptions we need 2 routes :
- One to get all the subscription with their options.
- One to add an option to an existing subscription.

Rules :
Option ROAMING can be only add to a MOBILE subscription
Option NETFLIX can be only add to a FIBRE subscription

## What we want to see

The goal of this exercise is to see how you manage Spring framework and hibernate.

We will be attentive at the code quality.

Keep it simple !

## Time

No time !

## Delivery

You must deliver a functional application.
Prefer using h2 database or docker environments ready to go.
Feel free to upgrade java version according your preference.