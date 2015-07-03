package gex.example.service

import gex.example.dto.v1.Hero
import gex.example.dto.v1.HeroPage
import gex.example.dto.v1.HeroPage

interface HeroService {

  Hero createHero(Hero hero)

  HeroPage listHeroes(Long from, Long size)

  Hero getHeroByName(String name)

  Hero updateHero(String name, Hero hero)

  def deleteHero(String name)
}
